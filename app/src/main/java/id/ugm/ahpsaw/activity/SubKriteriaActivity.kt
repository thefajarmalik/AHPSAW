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
        val spinner = findViewById<Spinner>(R.id.spinner_subkriteria)
        var namaKriteria = KRITERIA!!.nameOfCriteria
        var spinnerPos: Int = 0

        //SPINNER
        if (spinner != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item, namaKriteria
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
                        this@SubKriteriaActivity,
                        "Selected item: " +
                                "" + namaKriteria[position], Toast.LENGTH_SHORT
                    ).show()
                    spinnerPos = position
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }

        var Geografis: Array<String> =
            arrayOf("Latitude", "Ketinggian", "Panjang Jalan")
        var Meteorologis: Array<String> =
            arrayOf("Suhu", "Kecepatan Angin", "Kelembaban", "Presipitasi", "Tekanan Udara")

        var matriksGeografis: Array<DoubleArray> =
            Array(Geografis.size) { DoubleArray(Geografis.size) }
        var matriksMeteorologis: Array<DoubleArray> =
            Array(Meteorologis.size) { DoubleArray(Meteorologis.size) }

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
            MatriksElemen(matriksGeografis, Geografis, KRITERIA.calcAbsoluteWeight()[3])
        var kriteriaMeteorologis: MatriksElemen =
            MatriksElemen(matriksMeteorologis, Meteorologis, KRITERIA.calcAbsoluteWeight()[2])

        var weightMedis = DoubleArray(1) { KRITERIA.calcAbsoluteWeight()[0] }
        var weightDemografis = DoubleArray(1) { KRITERIA.calcAbsoluteWeight()[1] }

        var allWeight : Serializable =
            weightMedis + weightDemografis + kriteriaMeteorologis.calcAbsoluteWeight() + kriteriaGeografis.calcAbsoluteWeight()
        var s = allWeight
        //Log.i("CALCULATION TEST", allWeight.size.toString())

        var subkriteria = ArrayList<SubkriteriaData>()


        //APPLY RECYCLER VIEW
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@SubKriteriaActivity)
            adapter = SubkriteriaAdapter(subkriteria)
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


}