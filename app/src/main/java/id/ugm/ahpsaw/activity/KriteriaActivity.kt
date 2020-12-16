package id.ugm.ahpsaw.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.ugm.ahpsaw.R
import id.ugm.ahpsaw.adapter.HasilAdapter
import id.ugm.ahpsaw.adapter.KriteriaAdapter
import id.ugm.ahpsaw.data.HasilData
import id.ugm.ahpsaw.data.KriteriaData

class KriteriaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.kriteria)
        supportActionBar?.title ="Kriteria"
        val btn_alternatif: Button = findViewById(R.id.button_subkriteria) as Button
        btn_alternatif.setOnClickListener {
            val intent = Intent(this, SubKriteriaActivity::class.java)
            startActivity(intent)
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_kriteria)

        var kriteria = ArrayList<KriteriaData>()
        kriteria.add(KriteriaData("Kriteria 1", "Kriteria 2"))
        kriteria.add(KriteriaData("Kriteria 1", "Kriteria 3"))
        kriteria.add(KriteriaData("Kriteria 1", "Kriteria 4"))
        kriteria.add(KriteriaData("Kriteria 2", "Kriteria 3"))
        kriteria.add(KriteriaData("Kriteria 2", "Kriteria 4"))
        kriteria.add(KriteriaData("Kriteria 3", "Kriteria 4"))



        //APPLY RECYCLER VIEW
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@KriteriaActivity)
            adapter = KriteriaAdapter(kriteria)
        }
    }


}