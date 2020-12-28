package id.ugm.ahpsaw.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.ugm.ahpsaw.R
import id.ugm.ahpsaw.adapter.EmptyAdapter
import id.ugm.ahpsaw.adapter.SubkriteriaAdapter
import id.ugm.ahpsaw.data.MatriksElemen
import id.ugm.ahpsaw.data.SubkriteriaData


class SubKriteriaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.subkriteria)
        supportActionBar?.title = "Subkriteria"
        val intent = intent
        val KRITERIA = intent.getSerializableExtra("MatriksElemen") as? MatriksElemen
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_subkriteria)
        val btn_alternatif: Button = findViewById(R.id.button_alternatif) as Button
        val textview_CR: TextView = findViewById(R.id.CR_value_subkriteria) as TextView
        val btn_CR: Button = findViewById(R.id.button_CR_subkriteria) as Button
        val spinner = findViewById<Spinner>(R.id.spinner_subkriteria)
        val namaKriteria = KRITERIA!!.nameOfCriteria
        var spinnerPos = 0

        val parentWeightMedis = KRITERIA.calcAbsoluteWeight()[0]
        val parentWeightDemografis = KRITERIA.calcAbsoluteWeight()[1]
        val parentWeightMeteorologis = KRITERIA.calcAbsoluteWeight()[2]
        val parentWeightGeografis = KRITERIA.calcAbsoluteWeight()[3]


        val namaSubkriteriaGeografis: Array<String> =
            arrayOf("Latitude", "Ketinggian", "Panjang Jalan")
        val namaSubkriteriaMeteorologis: Array<String> =
            arrayOf("Suhu", "Kecepatan Angin", "Kelembaban", "Presipitasi", "Tekanan Udara")

        val matriksGeografis: Array<DoubleArray> =
            Array(namaSubkriteriaGeografis.size) { DoubleArray(namaSubkriteriaGeografis.size) }
        val matriksMeteorologis: Array<DoubleArray> =
            Array(namaSubkriteriaMeteorologis.size) { DoubleArray(namaSubkriteriaMeteorologis.size) }

        var subkriteriaMeteo = ArrayList<SubkriteriaData>()
        var subkriteriaGeo = ArrayList<SubkriteriaData>()

        addMatrixGeografis(matriksGeografis)
        addMatrixMeteorologis(matriksMeteorologis)

        matriksMeteorologis.forEachIndexed{i, item ->
            item.forEachIndexed{j, item2 ->
                Log.i("matriksMeteorologis[$i][$j]= ", item2.toString())
            }
        }


        var SUBKRITERIA_GEOGRAFIS: MatriksElemen

        var SUBKRITERIA_METEOROLOGIS: MatriksElemen

        SUBKRITERIA_METEOROLOGIS = MatriksElemen(matriksMeteorologis, namaSubkriteriaMeteorologis, parentWeightMeteorologis)
        SUBKRITERIA_GEOGRAFIS = MatriksElemen(matriksGeografis, namaSubkriteriaGeografis, parentWeightGeografis)

        var allWeight = ArrayList<DoubleArray>()
        allWeight.add(DoubleArray(1){parentWeightMedis})
        allWeight.add(DoubleArray(1){parentWeightDemografis})
        allWeight.add(SUBKRITERIA_METEOROLOGIS.calcAbsoluteWeight())
        allWeight.add(SUBKRITERIA_GEOGRAFIS.calcAbsoluteWeight())
        var w: DoubleArray

        if (spinner != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item, namaKriteria
            )
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
            spinner.adapter = adapter
            spinner.setSelection(3)
            spinner.setOnTouchListener{ v, event ->
                if (event.action == MotionEvent.ACTION_DOWN){
                    SUBKRITERIA_GEOGRAFIS = createMatrixFromUpdatedList(subkriteriaGeo, namaSubkriteriaGeografis, parentWeightGeografis, 3)
                    SUBKRITERIA_METEOROLOGIS = createMatrixFromUpdatedList(subkriteriaMeteo, namaSubkriteriaMeteorologis, parentWeightMeteorologis, 2)

                    SUBKRITERIA_GEOGRAFIS.comparison.forEachIndexed{i, item ->
                        item.forEachIndexed{j, item2 ->
                            Log.i("SPINNER IS TOUCHED, subkriteriaGeo[$i][$j]= ", item2.toString())
                        }
                    }

                    SUBKRITERIA_METEOROLOGIS.comparison.forEachIndexed{i, item ->
                        item.forEachIndexed{j, item2 ->
                            Log.i("SPINNER IS TOUCHED, subkriteriaMeteo[$i][$j]= ", item2.toString())
                        }
                    }

                    allWeight[2] = SUBKRITERIA_METEOROLOGIS.calcAbsoluteWeight()
                    allWeight[3] = SUBKRITERIA_GEOGRAFIS.calcAbsoluteWeight()
                    w = buildWeight(allWeight)
                    w.forEachIndexed { i, item ->
                        Log.i("SPINNER IS TOUCHED, ALLWEIGHT W[$i]= ", item.toString())
                    }
                }
                v.performClick()
            }
            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?, position: Int, id: Long
                ) {
                    spinnerPos = position
                    subkriteriaMeteo = fillSubKriteriaList(namaSubkriteriaMeteorologis, SUBKRITERIA_METEOROLOGIS.comparison)
                    subkriteriaGeo = fillSubKriteriaList(namaSubkriteriaGeografis, SUBKRITERIA_GEOGRAFIS.comparison)
                    when (position){
                        0 ->{
//                            addMatrixGeografis(matriksGeografis)
//                            addMatrixMeteorologis(matriksMeteorologis)
                            textview_CR.text = "Tidak ada"
                            recyclerView.apply {
                                layoutManager = LinearLayoutManager(this@SubKriteriaActivity)
                                this.adapter = EmptyAdapter("Jumlah subkriteria hanya 1 sehingga tidak perlu perbandingan.")
                            }
                        }
                        1 ->{
//                            addMatrixGeografis(matriksGeografis)
//                            addMatrixMeteorologis(matriksMeteorologis)
                            textview_CR.text = "Tidak ada"
                            recyclerView.apply {
                                layoutManager = LinearLayoutManager(this@SubKriteriaActivity)
                                this.adapter = EmptyAdapter("Jumlah subkriteria hanya 1 sehingga tidak perlu perbandingan.")
                            }
                        }
                        2 -> {
                            SUBKRITERIA_METEOROLOGIS.comparison.forEachIndexed{i, item ->
                                item.forEachIndexed{j, item2 ->
                                    Log.i("SPINNER METEOROLOGIS IS SELECTED, subkriteriaMeteo[$i][$j]= ", item2.toString())
                                }
                            }
                            textview_CR.text = "%10f".format(SUBKRITERIA_METEOROLOGIS.consistencyRatio())
                            updateRecyclerView(recyclerView, subkriteriaMeteo)
                        }
                        3 -> {
                            SUBKRITERIA_GEOGRAFIS.comparison.forEachIndexed{i, item ->
                                item.forEachIndexed{j, item2 ->
                                    Log.i("SPINNER GEOGRAFIS IS SELECTED, subkriteriaGeo[$i][$j]= ", item2.toString())
                                }
                            }
                            textview_CR.text = "%10f".format(SUBKRITERIA_GEOGRAFIS.consistencyRatio())
                            updateRecyclerView(recyclerView, subkriteriaGeo)
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }

        btn_CR.setOnClickListener {
            //2 meteo, 3 geo
            Log.i("SPINNER POS=", spinnerPos.toString())
            when (spinnerPos){
                0 ->{
                    textview_CR.text = "Tidak ada"
                }
                1 ->{
                    textview_CR.text = "Tidak ada"
                }
                2 ->{
                    SUBKRITERIA_GEOGRAFIS = createMatrixFromUpdatedList(subkriteriaGeo, namaSubkriteriaGeografis, parentWeightGeografis, 3)
                    SUBKRITERIA_METEOROLOGIS = createMatrixFromUpdatedList(subkriteriaMeteo, namaSubkriteriaMeteorologis, parentWeightMeteorologis, 2)
                    allWeight[2] = SUBKRITERIA_METEOROLOGIS.calcAbsoluteWeight()
                    allWeight[3] = SUBKRITERIA_GEOGRAFIS.calcAbsoluteWeight()
                    w = buildWeight(allWeight)

                    SUBKRITERIA_METEOROLOGIS.comparison.forEachIndexed{i, item ->
                        item.forEachIndexed{j, item2 ->
                            Log.i("BUTTON CR IS CLICKED, subkriteriaMeteo[$i][$j]= ", item2.toString())
                        }
                    }
                    SUBKRITERIA_GEOGRAFIS.comparison.forEachIndexed{i, item ->
                        item.forEachIndexed{j, item2 ->
                            Log.i("BUTTON CR IS CLICKED, subkriteriaGeo[$i][$j]= ", item2.toString())
                        }
                    }
                    textview_CR.text = "%10f".format(SUBKRITERIA_METEOROLOGIS.consistencyRatio())
                }
                3 ->{
                    SUBKRITERIA_GEOGRAFIS = createMatrixFromUpdatedList(subkriteriaGeo, namaSubkriteriaGeografis, parentWeightGeografis, 3)
                    SUBKRITERIA_METEOROLOGIS = createMatrixFromUpdatedList(subkriteriaMeteo, namaSubkriteriaMeteorologis, parentWeightMeteorologis, 2)
                    allWeight[2] = SUBKRITERIA_METEOROLOGIS.calcAbsoluteWeight()
                    allWeight[3] = SUBKRITERIA_GEOGRAFIS.calcAbsoluteWeight()
                    w = buildWeight(allWeight)

                    SUBKRITERIA_METEOROLOGIS.comparison.forEachIndexed{i, item ->
                        item.forEachIndexed{j, item2 ->
                            Log.i("BUTTON CR IS CLICKED, subkriteriaMeteo[$i][$j]= ", item2.toString())
                        }
                    }
                    SUBKRITERIA_GEOGRAFIS.comparison.forEachIndexed{i, item ->
                        item.forEachIndexed{j, item2 ->
                            Log.i("BUTTON CR IS CLICKED, subkriteriaGeo[$i][$j]= ", item2.toString())
                        }
                    }
                    textview_CR.text = "%10f".format(SUBKRITERIA_GEOGRAFIS.consistencyRatio())
                }
            }
        }


        btn_alternatif.setOnClickListener {
            val intentNext = Intent(this, AlternatifActivity::class.java)
            SUBKRITERIA_GEOGRAFIS = createMatrixFromUpdatedList(subkriteriaGeo, namaSubkriteriaGeografis, parentWeightGeografis, 3)
            SUBKRITERIA_METEOROLOGIS = createMatrixFromUpdatedList(subkriteriaMeteo, namaSubkriteriaMeteorologis, parentWeightMeteorologis, 2)
            allWeight[2] = SUBKRITERIA_METEOROLOGIS.calcAbsoluteWeight()
            allWeight[3] = SUBKRITERIA_GEOGRAFIS.calcAbsoluteWeight()
            w = buildWeight(allWeight)
            w.forEachIndexed { i, item ->
                Log.i("BTN_ALTERNATIF IS TOUCHED, ALLWEIGHT W[$i]= ", item.toString())
            }
            intentNext.putExtra("weight", w)
            startActivity(intentNext)
        }



    }
    fun buildWeight(x: ArrayList<DoubleArray>): DoubleArray{
        var n = 0
        x.forEachIndexed{ i, item ->
            n += item.size
        }
        var weight = DoubleArray(0)
        x.forEachIndexed{i, item ->
            weight += item
        }
        Log.i("Weight size= ", weight.size.toString())
        return weight
    }
    fun createMatrixFromUpdatedList(list: ArrayList<SubkriteriaData>, namaKriteria: Array<String>, parentWeight: Double, pos: Int): MatriksElemen{
        var matrix: Array<DoubleArray> = Array(namaKriteria.size) { DoubleArray(namaKriteria.size) }
        if (pos == 2){
            list.forEachIndexed{i, _ ->
                when (i) {
                    0 -> updateMatrix(0, 1, list, i, matrix)
                    1 -> updateMatrix(0, 2, list, i, matrix)
                    2 -> updateMatrix(0, 3, list, i, matrix)
                    3 -> updateMatrix(0, 4, list, i, matrix)
                    4 -> updateMatrix(1, 2, list, i, matrix)
                    5 -> updateMatrix(1, 3, list, i, matrix)
                    6 -> updateMatrix(1, 4, list, i, matrix)
                    7 -> updateMatrix(2, 3, list, i, matrix)
                    8 -> updateMatrix(2, 4, list, i, matrix)
                    9 -> updateMatrix(3, 4, list, i, matrix)
                }
            }
        }
        else if (pos == 3){
            list.forEachIndexed{i, _ ->
                when (i) {
                    0 -> updateMatrix(0, 1, list, i, matrix)
                    1 -> updateMatrix(0, 2, list, i, matrix)
                    2 -> updateMatrix(1, 2, list, i, matrix)
                }
            }
        }

        for (i in 0..namaKriteria.size-1){
            matrix[i][i] = 1.0
        }
        return MatriksElemen(matrix, namaKriteria, parentWeight)
    }

    private fun updateMatrix(i: Int, j: Int, kriteriaList: ArrayList<SubkriteriaData>, pos: Int, matriksToBeUpdated: Array<DoubleArray>) {
        if (kriteriaList[pos].reciprocal) {
            matriksToBeUpdated[j][i] = kriteriaList[pos].preferensi
            matriksToBeUpdated[i][j] = 1/kriteriaList[pos].preferensi
        }
        else {
            matriksToBeUpdated[i][j] = kriteriaList[pos].preferensi
            matriksToBeUpdated[j][i] = 1/kriteriaList[pos].preferensi
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
        return subKriteriaData
    }

    fun addMatrixGeografis(matriksGeografis: Array<DoubleArray>){
        Log.i("FUNCTION CALL CHECK: ", "addMatrixGeografis is called")
        matriksGeografis[0][0] = 1.0
        matriksGeografis[0][1] = 1.0
        matriksGeografis[0][2] = 0.2

        matriksGeografis[1][0] = 1.0
        matriksGeografis[1][1] = 1.0
        matriksGeografis[1][2] = 0.142857143

        matriksGeografis[2][0] = 5.0
        matriksGeografis[2][1] = 7.0
        matriksGeografis[2][2] = 1.0
    }

    fun addMatrixMeteorologis(matriksMeteorologis: Array<DoubleArray>){
        Log.i("FUNCTION CALL CHECK: ", "addMatrixMeteorologis is called")
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
    }


}