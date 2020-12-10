package id.ugm.ahpsaw.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.ugm.ahpsaw.R
import id.ugm.ahpsaw.adapter.AlternatifAdapter
import id.ugm.ahpsaw.data.AlternatifData


class AlternatifActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.alternatif)
        supportActionBar?.title ="Alternatif"

        val sumber_data = resources.getStringArray(R.array.sumber_data)
        val spinner = findViewById<Spinner>(R.id.spinner_alternatif)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_alternatif)

        var kab_semarang = ArrayList<AlternatifData>()
        kab_semarang.add(
            AlternatifData(
                "Getasan",
                787.0,
                787.0,
                -7.37639,
                -7.37639,
                1450.0,
                175.6,
                20.0,
                11.0,
                98.0,
                17.0,
                30.3
            )
        )
        kab_semarang.add(
            AlternatifData(
                "Suruh",
                14.0,
                944.0,
                -7.37639,
                -7.37639,
                944.0,
                211.0,
                31.0,
                7.0,
                98.0,
                20.0,
                31.0
            )
        )

        //APPLY RECYCLER VIEW
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@AlternatifActivity)
            adapter = AlternatifAdapter(kab_semarang)
        }


        //SPINNER
        if (spinner != null) {
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, sumber_data)
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
    }
}