package id.ugm.ahpsaw.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.ugm.ahpsaw.R
import id.ugm.ahpsaw.adapter.AlternatifAdapter
import id.ugm.ahpsaw.adapter.HasilAdapter
import id.ugm.ahpsaw.data.AlternatifData
import id.ugm.ahpsaw.data.HasilData

class HasilActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hasil)
        supportActionBar?.title ="Hasil"

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_hasil)

        var hasil = ArrayList<HasilData>()
        hasil.add(HasilData(1,"Ungaran Barat",0.932955341))
        hasil.add(HasilData(2,"Ambarawa",0.563961547))
        hasil.add(HasilData(4,"Suruh",0.541397218))
        hasil.add(HasilData(3,"Bawen",0.331630184))
        hasil.add(HasilData(5,"Getasan",0.258510557))

        //APPLY RECYCLER VIEW
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@HasilActivity)
            adapter = HasilAdapter(hasil)
        }
    }

}