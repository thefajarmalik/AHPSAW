package id.ugm.ahpsaw

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*

class SubKriteriaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.subkriteria)
        supportActionBar?.title ="Subkriteria"
        //BUTTON TO ALTERNATIF
        val btn_alternatif: Button = findViewById(R.id.button_alternatif) as Button
        btn_alternatif.setOnClickListener {
            val intent = Intent(this, AlternatifActivity::class.java)
            startActivity(intent)
        }
        //SPINNER
        val kriteria = resources.getStringArray(R.array.kriteria)
        val spinner = findViewById<Spinner>(R.id.spinner_subkriteria)
        if (spinner != null) {
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, kriteria)
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
            spinner.adapter = adapter
            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    Toast.makeText(this@SubKriteriaActivity,
                        "Selected item: "+
                                "" + kriteria[position], Toast.LENGTH_SHORT).show()
                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
    }


}