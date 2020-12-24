package id.ugm.ahpsaw.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.ugm.ahpsaw.R
import id.ugm.ahpsaw.adapter.KriteriaAdapter
import id.ugm.ahpsaw.data.KriteriaData
import id.ugm.ahpsaw.data.MatriksElemen

class KriteriaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.kriteria)
        supportActionBar?.title = "Kriteria"

        val btn_alternatif: Button = findViewById(R.id.button_subkriteria) as Button
        val btn_CR: Button = findViewById(R.id.button_CR_kriteria) as Button
        val textview_CR: TextView = findViewById(R.id.CR_value_kriteria) as TextView


        val recyclerView = findViewById<RecyclerView>(R.id.recycler_kriteria)

        val sumber_data = arrayListOf("Kabupaten Semarang", "Isi Manual")
        var kriteria = ArrayList<KriteriaData>()

        val spinner = findViewById<Spinner>(R.id.spinner_sumber_data_kriteria)
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
                    Toast.makeText(
                        this@KriteriaActivity,
                        "Selected item: " +
                                "" + sumber_data[position], Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }

        var namaKriteria: Array<String> =
            arrayOf("Medis", "Demografis", "Meteorologis", "Geografis")


        var matrix: Array<DoubleArray> = Array(namaKriteria.size) { DoubleArray(namaKriteria.size) }
        matrix[0][0] = 1.0
        matrix[0][1] = 2.0
        matrix[0][2] = 5.0
        matrix[0][3] = 4.0

        matrix[1][0] = 0.5
        matrix[1][1] = 1.0
        matrix[1][2] = 3.0
        matrix[1][3] = 3.0

        matrix[2][0] = 0.2
        matrix[2][1] = 0.333333333333
        matrix[2][2] = 1.0
        matrix[2][3] = 1.0

        matrix[3][0] = 0.25
        matrix[3][1] = 0.333333333333
        matrix[3][2] = 1.0
        matrix[3][3] = 1.0

        var KRITERIA: MatriksElemen = MatriksElemen(matrix, namaKriteria)
        var weight = KRITERIA.calcAbsoluteWeight()
        btn_CR.setOnClickListener {
            textview_CR.text = "%10f".format(KRITERIA.consistencyRatio())
        }

//        for (i in 0..namaKriteria.size - 1) {
//            for (j in 0..namaKriteria.size - 1) {
//                if (j > i) {
//                    kriteria.add(KriteriaData(namaKriteria[i], namaKriteria[j]))
//                }
//            }
//        }


        //APPLY RECYCLER VIEW
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@KriteriaActivity)
            adapter = KriteriaAdapter(kriteria)
        }
        btn_alternatif.setOnClickListener {
            val intent = Intent(this, SubKriteriaActivity::class.java)
            intent.putExtra("MatriksElemen", KRITERIA)
            startActivity(intent)
        }
    }


}