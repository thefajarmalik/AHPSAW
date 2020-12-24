package id.ugm.ahpsaw.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.ugm.ahpsaw.R
import id.ugm.ahpsaw.adapter.AlternatifAdapter
import id.ugm.ahpsaw.data.AlternatifData
import id.ugm.ahpsaw.data.HasilData
import java.io.Serializable


class AlternatifActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.alternatif)
        supportActionBar?.title = "Alternatif"
        var intent = intent
        var allweight = intent.getSerializableExtra("weight") as? DoubleArray
//        Log.i("ALLWEIGHT ALTERNATIF", allweight?.size.toString())

        val btn_hasil: Button = findViewById(R.id.button_hasil) as Button
        val sumber_data = arrayListOf("Kabupaten Semarang", "Isi Manual")
        val spinner = findViewById<Spinner>(R.id.spinner_alternatif)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_alternatif)
        var kab_semarang = ArrayList<AlternatifData>()

        spinnerAdapter(spinner, sumber_data)

        addAlternatifKabSmg(kab_semarang)


        var matriks = normalizeAlternatif(kab_semarang)
//        Log.i("MATRIX NORMALIZATION TEST", "matriks[0][0]: " + matriks[4][9])
        var hasil: Serializable = calcHasil(matriks, kab_semarang, allweight!!)


        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@AlternatifActivity)
            adapter = AlternatifAdapter(kab_semarang)
        }

        btn_hasil.setOnClickListener {
            val intent = Intent(this, HasilActivity::class.java)
            intent.putExtra("hasil", hasil)
            startActivity(intent)
        }
    }

    private fun calcHasil(
        matriks: Array<DoubleArray>,
        alternatif: ArrayList<AlternatifData>,
        weight: DoubleArray
    ): ArrayList<HasilData> {
        var skor: DoubleArray = DoubleArray(matriks.size)
        var hasilData = ArrayList<HasilData>()
        for (i in 0..skor.size-1)
            skor[i] = 0.0
        for (i in 0..matriks.size - 1) {
            for (j in 0..matriks[i].size-1) {
                skor[i] += matriks[i][j]*(weight[j])
            }
//            Log.i("PERHITUNGAN SKOR: ","SKOR[$i]=" + skor[i])
        }
        for (i in 0..matriks.size - 1) {
            hasilData.add(HasilData(alternatif.elementAt(i).nama,skor[i]))
        }
        hasilData.sortByDescending { it.skor }
        return hasilData
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
            matriks[i][7] = alternatif.elementAt(i).latitude
            matriks[i][8] = alternatif.elementAt(i).ketinggian
            matriks[i][9] = alternatif.elementAt(i).panjangJalan
        }
        for (i in 0..alternatif.size - 1) {
            for (j in 0..9) {
                temp.set(i, DoubleArray(1){matriks[i][j]})
            }
        }


        var maxIndices: Array<Int> = arrayOf(0, 1, 3, 5, 6, 9)
        var minIndices: Array<Int> = arrayOf(2, 4, 7, 8)

        var maxMin = DoubleArray(matriks[0].size)
        for (i in 0..maxMin.size-1){
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


//        for (i in 0..alternatif.size - 1) {
//            for (j in 0..9) {
//                Log.i("After finding maxMin matriks[$i][$j]=", matriks[i][j].toString())
//            }
//        }

        for (i in 0..alternatif.size - 1) {
            for (j in 0..9) {
                if (maxIndices.contains(j)) {
//                    Log.i(
//                        "Pembagian matriks[$i][$j]: ",
//                        matriks[i][j].toString() + " DIBAGI " + maxMin[j].toString()
//                    )
                    matriks[i][j] = matriks[i][j] / maxMin[j]
//                    Log.i("matriks[$i][$j]=", matriks[i][j].toString())
                } else if (minIndices.contains(j)) {
//                    Log.i(
//                        "Pembagian matriks[$i][$j]: ",
//                        maxMin[j].toString() + " DIBAGI " + matriks[i][j].toString()
//                    )
                    matriks[i][j] = maxMin[j] / matriks[i][j]
//                    Log.i("Matriks[$i][$j]=", matriks[i][j].toString())
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
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
    }

    private fun addAlternatifKabSmg(kab_semarang: ArrayList<AlternatifData>) {
        kab_semarang.add(
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
        kab_semarang.add(
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
        kab_semarang.add(
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
        kab_semarang.add(
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
        kab_semarang.add(
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