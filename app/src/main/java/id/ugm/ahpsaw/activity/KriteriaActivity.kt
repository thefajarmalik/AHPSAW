package id.ugm.ahpsaw.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.ugm.ahpsaw.R
import id.ugm.ahpsaw.adapter.KriteriaAdapter
import id.ugm.ahpsaw.data.AlternatifData
import id.ugm.ahpsaw.data.KriteriaData
import id.ugm.ahpsaw.data.MatriksElemen

class KriteriaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.kriteria)
        supportActionBar?.title = "Kriteria"

        val btn_subkriteria: Button = findViewById(R.id.button_subkriteria) as Button
        val btn_CR: Button = findViewById(R.id.button_CR_kriteria) as Button
        val textview_CR: TextView = findViewById(R.id.CR_value_kriteria) as TextView

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_kriteria)

        var kriteria :ArrayList<KriteriaData>
        var namaKriteria: Array<String> =
            arrayOf("Medis", "Demografis", "Meteorologis", "Geografis")

        var matrix: Array<DoubleArray> = Array(namaKriteria.size) { DoubleArray(namaKriteria.size) }
        addMatrixKriteria(matrix)


        var KRITERIA: MatriksElemen 
        kriteria = fillKriteriaList(namaKriteria, matrix)
        updateRecyclerView(recyclerView, kriteria)

        kriteria.forEachIndexed { i, item ->
            Log.i("kriteria on create", "Kriteria[$i]=" + item)
        }

        btn_CR.setOnClickListener {
            KRITERIA = createMatrixFromUpdatedList(kriteria, namaKriteria)
            kriteria.forEachIndexed { i, item ->
                Log.i("kriteria on CR CLICK", "Kriteria[$i]=" + item)
            }
            textview_CR.text = "%10f".format(KRITERIA.consistencyRatio())
        }

        btn_subkriteria.setOnClickListener {
            KRITERIA = createMatrixFromUpdatedList(kriteria, namaKriteria)
            val cr = KRITERIA.consistencyRatio()
            if (cr > 0.1){
                val builder = androidx.appcompat.app.AlertDialog.Builder(this)
                builder.setMessage("Consistency Ratio melebihi 0,1.")
                    .setTitle("Apakah Anda yakin untuk melanjutkan?")
                    .setCancelable(true)
                    .setPositiveButton("Ya") { dialog, id ->
                        val intent = Intent(this, SubKriteriaActivity::class.java)
                        KRITERIA = createMatrixFromUpdatedList(kriteria, namaKriteria)
                        intent.putExtra("MatriksElemen", KRITERIA)
                        startActivity(intent)
                    }
                    .setNegativeButton("Tidak") { dialog, id ->
                        // Dismiss the dialog
                        dialog.dismiss()
                    }
                val alert = builder.create()
                alert.show()
            }
            else {
                val intent = Intent(this, SubKriteriaActivity::class.java)
                KRITERIA = createMatrixFromUpdatedList(kriteria, namaKriteria)
                intent.putExtra("MatriksElemen", KRITERIA)
                startActivity(intent)
            }
        }

    }

    fun createMatrixFromUpdatedList(list: ArrayList<KriteriaData>, namaKriteria: Array<String>): MatriksElemen{
        var matrix: Array<DoubleArray> = Array(namaKriteria.size) { DoubleArray(namaKriteria.size) }
        list.forEachIndexed{i, _ ->
            when (i) {
                0 -> updateMatrixAndList(0, 1, list, i, matrix)
                1 -> updateMatrixAndList(0, 2, list, i, matrix)
                2 -> updateMatrixAndList(0, 3, list, i, matrix)
                3 -> updateMatrixAndList(1, 2, list, i, matrix)
                4 -> updateMatrixAndList(1, 3, list, i, matrix)
                5 -> updateMatrixAndList(2, 3, list, i, matrix)
            }
        }
        for (i in 0..namaKriteria.size-1){
            matrix[i][i] = 1.0
        }
        var KRITERIA = MatriksElemen(matrix, namaKriteria)
        return KRITERIA
    }

    private fun updateMatrixAndList(
        i: Int,
        j: Int,
        kriteriaList: ArrayList<KriteriaData>,
        pos: Int,
        matriksToBeUpdated: Array<DoubleArray>
    ) {
        if (kriteriaList[pos].reciprocal) {
            matriksToBeUpdated[j][i] = kriteriaList[pos].preferensi
            matriksToBeUpdated[i][j] = 1/kriteriaList[pos].preferensi
        }
        else {
            matriksToBeUpdated[i][j] = kriteriaList[pos].preferensi
            matriksToBeUpdated[j][i] = 1/kriteriaList[pos].preferensi
        }

    }



    fun updateRecyclerView(recyclerView: RecyclerView, kriteria: ArrayList<KriteriaData>){
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@KriteriaActivity)
            adapter = KriteriaAdapter(kriteria)
        }
    }

    fun fillKriteriaList(
        namaKriteria: Array<String>,
        matrix: Array<DoubleArray>
    ): ArrayList<KriteriaData> {
        var kriteriaData = ArrayList<KriteriaData>()
        for (i in 0..namaKriteria.size - 1) {
            for (j in 0..namaKriteria.size - 1) {
                if (j > i) {
                    if (matrix[i][j] < 1.0) {
                        kriteriaData.add(
                            KriteriaData(
                                namaKriteria[i],
                                namaKriteria[j],
                                true,
                                matrix[j][i]
                            )
                        )
                    } else {
                        kriteriaData.add(
                            KriteriaData(
                                namaKriteria[i],
                                namaKriteria[j],
                                false,
                                matrix[i][j]
                            )
                        )
                    }
                }
            }
        }
        return kriteriaData
    }
    fun addMatrixKriteria(matrix: Array<DoubleArray>){
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
    }


}
//        if (spinner != null) {
//            val adapter = ArrayAdapter(
//                this,
//                android.R.layout.simple_spinner_item, sumber_data
//            )
//            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
//            spinner.adapter = adapter
//            spinner.setSelection(0)
//            spinner.onItemSelectedListener = object :
//                AdapterView.OnItemSelectedListener {
//                override fun onItemSelected(
//                    parent: AdapterView<*>,
//                    view: View?, position: Int, id: Long
//                ) {
//                    when (position){
//                        0 -> {
//                            kriteria = fillKriteriaList(namaKriteria, matrix)
//                            updateRecyclerView(recyclerView, kriteria)
//                        }
//                        else -> {
//                            kriteria.clear()
//                            updateRecyclerView(recyclerView, kriteria)
//                        }
//                    }
//
//                }
//
//                override fun onNothingSelected(parent: AdapterView<*>) {
//                    // write code to perform some action
//                }
//            }
//        }