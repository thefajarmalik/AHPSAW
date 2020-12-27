package id.ugm.ahpsaw.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.ugm.ahpsaw.R
import id.ugm.ahpsaw.adapter.HasilAdapter
import id.ugm.ahpsaw.data.HasilData
import org.nield.kotlinstatistics.Centroid
import org.nield.kotlinstatistics.multiKMeansCluster

class HasilActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hasil)
        supportActionBar?.title = "Hasil"
        var intent = intent
        var hasil = intent.getSerializableExtra("hasil") as ArrayList<HasilData>
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_hasil)
        val clusters = hasil.multiKMeansCluster(
            k = 4,
            maxIterations = 10000,
            trialCount = 50,
            xSelector = { it.skor },
            ySelector = { it.skor })


        val hasilData = sortCluster(clusters)
        hasilData.forEach { item ->
            Log.i("hasilData: ", item.toString())
        }


        //APPLY RECYCLER VIEW
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@HasilActivity)
            adapter = HasilAdapter(hasilData)
        }
    }

    fun sortCluster(cluster: List<Centroid<HasilData>>) : ArrayList<HasilData> {
        var hasilData = ArrayList<HasilData>()
        val hasilClustered = cluster.sortedByDescending {it.points.first().skor }
        hasilClustered.forEachIndexed { i, item ->
            item.points.forEachIndexed { j, points ->
                points.zona = i
                hasilData.add(points)
            }
        }
        return hasilData
    }


}
