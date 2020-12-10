package id.ugm.ahpsaw.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import id.ugm.ahpsaw.R

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
    }


}