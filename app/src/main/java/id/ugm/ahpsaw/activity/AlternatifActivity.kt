package id.ugm.ahpsaw.activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.okhttp.Callback
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response
import id.ugm.ahpsaw.R
import id.ugm.ahpsaw.adapter.AlternatifAdapter
import id.ugm.ahpsaw.adapter.EmptyAdapter
import id.ugm.ahpsaw.data.AlternatifData
import id.ugm.ahpsaw.data.HasilData
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.CountDownLatch
import kotlin.math.absoluteValue


class AlternatifActivity : AppCompatActivity()  {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.alternatif)
        supportActionBar?.title = "Alternatif"

        val API_KEY: String = "2853cc1670b449099df210719201610"
        var intent = intent
        val allweight = intent.getSerializableExtra("weight") as DoubleArray
        allweight.forEachIndexed { i, item ->
            Log.i("WEIGHT[$i]=", item.toString())
        }
        val btn_hasil: Button = findViewById(R.id.button_hasil) as Button
        val btn_tambah: Button = findViewById(R.id.button_tambah_alternatif) as Button
        val sumber_data = arrayListOf("Kabupaten Semarang", "Isi Manual")
        val spinner = findViewById<Spinner>(R.id.spinner_alternatif)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_alternatif)
        var alternatifList = ArrayList<AlternatifData>()

        val client = OkHttpClient()

        var test_db = ArrayList<AlternatifData>()
        val db = FirebaseFirestore.getInstance()
        val collectionReference = db.collection("alternatif")
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Mengunduh data...")
        progressDialog.setMessage("Sedang mengunduh data dari database & API. Mohon menunggu.")
        progressDialog.show()
        collectionReference
            .get()
            .addOnSuccessListener { result ->
                for (document in result){
                    val data = document.toObject(AlternatifData::class.java)
                    test_db.add(
                        AlternatifData(
                        data.nama,
                        data.positif,
                        data.kepadatan,
                        data.suhu,
                        data.kecepatanAngin,
                        data.kelembaban,
                        data.presipitasi,
                        data.tekananUdara,
                        data.longitude,
                        data.latitude,
                        data.ketinggian,
                        data.panjangJalan
                    )
                    )
                }
                alternatifList = test_db.clone() as ArrayList<AlternatifData>
                alternatifList.forEachIndexed{i, item ->
                    updateAlternatifFromAPI(client, API_KEY, item)
                    Log.i("alternatifList [$i]: ", item.toString())
                }
                updateRecyclerView(recyclerView, alternatifList)
                toast("Database berhasil dimuat")
                progressDialog.dismiss()
            }
            .addOnFailureListener { e ->
                toast("Database gagal dimuat")
                progressDialog.dismiss()
            }


        //addAlternatifKabSmg(alternatifList)
        //alternatifList = test_db.clone() as ArrayList<AlternatifData>
        spinnerAdapter(spinner, sumber_data)
        updateRecyclerView(recyclerView, alternatifList)


        btn_tambah.setOnClickListener{
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_alternatif_edit, null)
            //mDialogView.findViewById<Button>(R.id.alternatif_edit_dialog_simpanbtn).setText("TAMBAH")
            val mAlertDialogBuilder = androidx.appcompat.app.AlertDialog.Builder(this)
                .setView(mDialogView)
                .setTitle("Tambah Alternatif")
                .setNegativeButton("BATAL") { dialog, id ->
                    dialog.dismiss()
                }
                .setPositiveButton("TAMBAH"){dialog, id ->
                    val alternatif = AlternatifData(
                        mDialogView.findViewById<EditText>(R.id.alternatif_dialog_nama).text.toString(),
                        mDialogView.findViewById<EditText>(R.id.alternatif_dialog_positif).text.toString().toDouble(),
                        mDialogView.findViewById<EditText>(R.id.alternatif_dialog_kepadatan).text.toString().toDouble(),
                        mDialogView.findViewById<EditText>(R.id.alternatif_dialog_suhu).text.toString().toDouble(),
                        mDialogView.findViewById<EditText>(R.id.alternatif_dialog_kecepatanangin).text.toString().toDouble(),
                        mDialogView.findViewById<EditText>(R.id.alternatif_dialog_kelembaban).text.toString().toDouble(),
                        mDialogView.findViewById<EditText>(R.id.alternatif_dialog_presipitasi).text.toString().toDouble(),
                        mDialogView.findViewById<EditText>(R.id.alternatif_dialog_tekananudara).text.toString().toDouble(),
                        1.0,
//                        mDialogView.findViewById<EditText>(R.id.alternatif_dialog_Longitude).text.toString().toDouble(),
                        mDialogView.findViewById<EditText>(R.id.alternatif_dialog_latitude).text.toString().toDouble(),
                        mDialogView.findViewById<EditText>(R.id.alternatif_dialog_ketinggian).text.toString().toDouble(),
                        mDialogView.findViewById<EditText>(R.id.alternatif_dialog_panjangjalan).text.toString().toDouble()
                    )
                    alternatifList.add(alternatif)
                    recyclerView.adapter?.notifyItemInserted(alternatifList.lastIndex)
                    recyclerView.adapter?.notifyItemRangeChanged(alternatifList.lastIndex, alternatifList.size)
                }
            val mAlertDialog = mAlertDialogBuilder.show()

        }

        btn_hasil.setOnClickListener {
            if (alternatifList.size >= 4) {
                val matriks = normalizeAlternatif(alternatifList)
                val hasil = calcHasil(matriks, alternatifList, allweight)
                val intentNext = Intent(this, HasilActivity::class.java)
                intentNext.putExtra("hasil", hasil)
                startActivity(intentNext)
            }
            else {
                toast("Cacah alternatif kurang dari jumlah cluster(4) !")
            }
        }
    }
    fun updateRecyclerView(recyclerView: RecyclerView, alternatifList: ArrayList<AlternatifData>){
        recyclerView.apply {
            Log.i("ALTERNATIF ACTIVITY", "recyclerView.apply is called")
            layoutManager = LinearLayoutManager(this@AlternatifActivity)
            adapter = AlternatifAdapter(alternatifList)
            if (alternatifList.isNotEmpty())
                adapter = AlternatifAdapter(alternatifList)
            else
                adapter = EmptyAdapter("Jumlah alternatif hanya 1 sehingga tidak perlu perbandingan.")

        }
    }

    fun updateAlternatifFromAPI(client: OkHttpClient, API_KEY: String, alternatif: AlternatifData){
        val url = "http://api.weatherapi.com/v1/current.json?key=${API_KEY}&q=${alternatif.latitude},${alternatif.longitude}"
        val request = Request.Builder()
            .url(url)
            .build()
        val call = client.newCall(request)
        val countDownLatch = CountDownLatch(1)
        call.enqueue(object : com.squareup.okhttp.Callback{
            override fun onResponse(response: Response?) {
                val jsonData: String? = response?.body()?.string()
                val jsonObject: JSONObject = JSONObject(jsonData)
                alternatif.suhu = jsonObject.getJSONObject("current").getDouble("temp_c")
                alternatif.kecepatanAngin = jsonObject.getJSONObject("current").getDouble("wind_kph")
                alternatif.kelembaban = jsonObject.getJSONObject("current").getDouble("humidity")
                alternatif.presipitasi = jsonObject.getJSONObject("current").getDouble("precip_mm")
                alternatif.tekananUdara = jsonObject.getJSONObject("current").getDouble("pressure_in")
                Log.i("API RUN", "alternatifData=${alternatif}")
                countDownLatch.countDown()
            }

            override fun onFailure(request: Request?, e: IOException?) {
                toast("Request API gagal")
                Log.i("API RUN", "onFailure")
                countDownLatch.countDown()
            }
        })
        countDownLatch.await()
    }

    private fun calcHasil(matriks: Array<DoubleArray>, alternatif: ArrayList<AlternatifData>, weight: DoubleArray): ArrayList<HasilData> {
        var skor: DoubleArray = DoubleArray(matriks.size)
        var hasilData = ArrayList<HasilData>()
        for (i in skor.indices)
            skor[i] = 0.0
        for (i in matriks.indices) {
            for (j in 0 until matriks[0].size) {
                var s = skor[i]
                skor[i] += matriks[i][j] * (weight[j])
                Log.i("calcHasil[$i]: ", "${s} += ${matriks[i][j]} +* ${weight[j]} = ${skor[i]}")
            }
        }
        for (i in matriks.indices) {
            hasilData.add(HasilData(alternatif.elementAt(i).nama, skor[i], 0))
        }
        hasilData.sortByDescending { it.skor }
        return hasilData
    }

    private fun toast(s: String){
        if (s.isNotEmpty())
            Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }

    private fun normalizeAlternatif(alternatif: ArrayList<AlternatifData>): Array<DoubleArray> {
        var temp: Array<DoubleArray> = Array(alternatif.size) { DoubleArray(10) }
        var matriks: Array<DoubleArray> = Array(alternatif.size) { DoubleArray(10) }
        for (i in 0..alternatif.size - 1) {
            matriks[i][0] = alternatif.elementAt(i).positif
            matriks[i][1] = alternatif.elementAt(i).kepadatan
            matriks[i][2] = alternatif.elementAt(i).suhu
            matriks[i][3] = alternatif.elementAt(i).kecepatanAngin
            matriks[i][4] = alternatif.elementAt(i).kelembaban
            matriks[i][5] = alternatif.elementAt(i).presipitasi
            matriks[i][6] = alternatif.elementAt(i).tekananUdara
            matriks[i][7] = alternatif.elementAt(i).latitude.absoluteValue
            matriks[i][8] = alternatif.elementAt(i).ketinggian
            matriks[i][9] = alternatif.elementAt(i).panjangJalan
        }
        for (i in 0..alternatif.size - 1) {
            for (j in 0..9) {
                temp.set(i, DoubleArray(1) { matriks[i][j] })
            }
        }


        var maxIndices: Array<Int> = arrayOf(0, 1, 3, 5, 6, 7, 9)
        var minIndices: Array<Int> = arrayOf(2, 4, 8)

        var maxMin = DoubleArray(matriks[0].size)
        matriks[0].forEachIndexed{i, item ->
            maxMin[i] = matriks[0][i]
        }
        for (i in 0..alternatif.size - 1) {
            for (j in 0..9) {
                if (maxIndices.contains(j)) {
                    if (matriks[i][j] > maxMin[j]) {
                        maxMin[j] = matriks[i][j]
                    }
                }
                if (minIndices.contains(j)) {
                    if (matriks[i][j] < maxMin[j]) {
                        maxMin[j] = matriks[i][j]
                    }
                }
            }
        }


        for (i in 0..alternatif.size - 1) {
            for (j in 0..9) {
                if (maxIndices.contains(j)) {
                    matriks[i][j] = matriks[i][j] / maxMin[j]
                    Log.i("normalizeAlternatif(max)= ", "matriks[$i][$j]= ${matriks[i][j]}")
                } else if (minIndices.contains(j)) {
                    matriks[i][j] = maxMin[j] / matriks[i][j]
                    Log.i("normalizeAlternatif(min)= ", "matriks[$i][$j]= ${matriks[i][j]}")
                }
            }
        }
        return matriks
    }

    private fun spinnerAdapter(spinner: Spinner, sumber_data: ArrayList<String>) {
        if (spinner != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item, sumber_data
            )
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?, position: Int, id: Long
                ) {
                    if (position == 0){
                        //DATA PREDEFINED
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
    }

    private fun addAlternatifKabSmg(alternatifList: ArrayList<AlternatifData>) {
        alternatifList.add(
            AlternatifData(
                "Getasan",
                1.0,
                787.0,
                20.0,
                11.0,
                98.0,
                17.0,
                30.3,
                1.0,
                -7.376397,
                1450.0,
                175.6
            )
        )
        alternatifList.add(
            AlternatifData(
                "Suruh",
                14.0,
                944.0,
                31.0,
                7.0,
                98.0,
                20.0,
                31.0,
                1.0,
                -7.36729,
                944.0,
                211.0
            )
        )

        alternatifList.add(
            AlternatifData(
                "Ungaran Barat",
                95.0,
                2470.0,
                37.0,
                8.0,
                97.0,
                40.0,
                30.3,
                1.0,
                -7.129417,
                318.0,
                153.0
            )
        )
        alternatifList.add(
            AlternatifData(
                "Bawen",
                54.0,
                1388.0,
                29.0,
                5.0,
                98.0,
                24.0,
                30.0,
                1.0,
                -7.223682,
                650.0,
                44.2
            )
        )
        alternatifList.add(
            AlternatifData(
                "Ambarawa",
                29.0,
                2258.0,
                23.0,
                9.0,
                98.0,
                24.0,
                30.3,
                1.0,
                -7.255641,
                514.0,
                146.0
            )
        )
    }
}
