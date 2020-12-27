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
import id.ugm.ahpsaw.adapter.KriteriaAdapter
import id.ugm.ahpsaw.adapter.SubkriteriaAdapter
import id.ugm.ahpsaw.data.KriteriaData
import id.ugm.ahpsaw.data.MatriksElemen
import id.ugm.ahpsaw.data.SubkriteriaData
import java.io.Serializable

class SubKriteriaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.subkriteria)
        supportActionBar?.title = "Subkriteria"
        var intent = intent
        var KRITERIA = intent.getSerializableExtra("MatriksElemen") as? MatriksElemen
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_subkriteria)
        val btn_alternatif: Button = findViewById(R.id.button_alternatif) as Button
        val textview_CR: TextView = findViewById(R.id.CR_value_subkriteria) as TextView
        val btn_CR: Button = findViewById(R.id.button_CR_subkriteria) as Button
        var spinner = findViewById<Spinner>(R.id.spinner_subkriteria)
        var namaKriteria = KRITERIA!!.nameOfCriteria
        var spinnerPos: Int = 0


        var namaSubkriteriaGeografis: Array<String> =
            arrayOf("Latitude", "Ketinggian", "Panjang Jalan")
        var namaSubkriteriaMeteorologis: Array<String> =
            arrayOf("Suhu", "Kecepatan Angin", "Kelembaban", "Presipitasi", "Tekanan Udara")

        var matriksGeografis: Array<DoubleArray> =
            Array(namaSubkriteriaGeografis.size) { DoubleArray(namaSubkriteriaGeografis.size) }
        var matriksMeteorologis: Array<DoubleArray> =
            Array(namaSubkriteriaMeteorologis.size) { DoubleArray(namaSubkriteriaMeteorologis.size) }

        matriksGeografis[0][0] = 1.0
        matriksGeografis[0][1] = 1.0
        matriksGeografis[0][2] = 0.2

        matriksGeografis[1][0] = 1.0
        matriksGeografis[1][1] = 1.0
        matriksGeografis[1][2] = 0.142857143

        matriksGeografis[2][0] = 5.0
        matriksGeografis[2][1] = 7.0
        matriksGeografis[2][2] = 1.0


        matriksMeteorologis[0][0] = 1.0
        matriksMeteorologis[0][1] = 0.333333333
        matriksMeteorologis[0][2] = 2.0
        matriksMeteorologis[0][3] = 4.0
        matriksMeteorologis[0][4] = 3.0

        matriksMeteorologis[1][0] = 3.0
        matriksMeteorologis[1][1] = 1.0
        matriksMeteorologis[1][2] = 3.0
        matriksMeteorologis[1][3] = 7.0
        matriksMeteorologis[1][4] = 5.0

        matriksMeteorologis[2][0] = 0.5
        matriksMeteorologis[2][1] = 0.333333333
        matriksMeteorologis[2][2] = 1.0
        matriksMeteorologis[2][3] = 3.0
        matriksMeteorologis[2][4] = 2.0

        matriksMeteorologis[3][0] = 0.25
        matriksMeteorologis[3][1] = 0.142857143
        matriksMeteorologis[3][2] = 0.333333333
        matriksMeteorologis[3][3] = 1.0
        matriksMeteorologis[3][4] = 0.5

        matriksMeteorologis[4][0] = 0.333333333
        matriksMeteorologis[4][1] = 0.2
        matriksMeteorologis[4][2] = 0.5
        matriksMeteorologis[4][3] = 2.0
        matriksMeteorologis[4][4] = 1.0


        var kriteriaGeografis: MatriksElemen =
            MatriksElemen(matriksGeografis, namaSubkriteriaGeografis, KRITERIA.calcAbsoluteWeight()[3])
        var kriteriaMeteorologis: MatriksElemen =
            MatriksElemen(matriksMeteorologis, namaSubkriteriaMeteorologis, KRITERIA.calcAbsoluteWeight()[2])

        var weightMedis = DoubleArray(1) { KRITERIA.calcAbsoluteWeight()[0] }
        var weightDemografis = DoubleArray(1) { KRITERIA.calcAbsoluteWeight()[1] }

        var allWeight : Serializable =
            weightMedis + weightDemografis + kriteriaMeteorologis.calcAbsoluteWeight() + kriteriaGeografis.calcAbsoluteWeight()
        var s = allWeight

        var subkriteria = ArrayList<SubkriteriaData>()

        if (spinner != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item, namaKriteria
            )
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
            spinner.adapter = adapter
            spinner.setSelection(3)
            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?, position: Int, id: Long
                ) {
                    spinnerPos = position
                    when (position){
                        2 -> {
                            subkriteria = fillSubKriteriaList(namaSubkriteriaMeteorologis, matriksMeteorologis)
                            updateRecyclerView(recyclerView, subkriteria)
                        }
                        3 -> {
                            subkriteria = fillSubKriteriaList(namaSubkriteriaGeografis, matriksGeografis)
                            updateRecyclerView(recyclerView, subkriteria)
                        }
                        else -> {
                            subkriteria.clear()
                            updateRecyclerView(recyclerView, subkriteria)
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }


        btn_alternatif.setOnClickListener {
            val intent = Intent(this, AlternatifActivity::class.java)
            intent.putExtra("weight", allWeight)
            startActivity(intent)
        }

        btn_CR.setOnClickListener {
            //2 meteo, 3 geo
            if (spinnerPos == 2) textview_CR.text =
                "%10f".format(kriteriaMeteorologis?.consistencyRatio())
            if (spinnerPos == 3) textview_CR.text =
                "%10f".format(kriteriaGeografis?.consistencyRatio())
        }

    }

    fun updateRecyclerView(recyclerView: RecyclerView, subkriteria: ArrayList<SubkriteriaData>){
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@SubKriteriaActivity)
            adapter = SubkriteriaAdapter(subkriteria)
        }
    }

    fun fillSubKriteriaList(namaKriteria: Array<String>, matrix: Array<DoubleArray>): ArrayList<SubkriteriaData> {
        var subKriteriaData = ArrayList<SubkriteriaData>()
        for (i in 0..namaKriteria.size - 1) {
            for (j in 0..namaKriteria.size - 1) {
                if (j > i) {
                    if (matrix[i][j] < 1.0){
                        subKriteriaData.add(SubkriteriaData(namaKriteria[i], namaKriteria[j], true, matrix[j][i]))
                    }
                    else {
                        subKriteriaData.add(SubkriteriaData(namaKriteria[i], namaKriteria[j], false, matrix[i][j]))
                    }
                }
            }
        }
        subKriteriaData.forEachIndexed { i, item ->
            Log.i("SUBKRITERIA[$i]: ", item.toString())
        }
        return subKriteriaData
    }


}