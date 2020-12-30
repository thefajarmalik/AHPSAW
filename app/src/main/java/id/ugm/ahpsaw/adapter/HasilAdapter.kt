package id.ugm.ahpsaw.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.ugm.ahpsaw.R
import id.ugm.ahpsaw.data.HasilData

class HasilAdapter(private val list: ArrayList<HasilData>) :
    RecyclerView.Adapter<HasilAdapter.HasilViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HasilViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_hasil, parent, false)
        return HasilViewHolder(view)
    }

    override fun onBindViewHolder(holder: HasilViewHolder, position: Int) {
        (holder as HasilViewHolder).bind(list[position])
    }

    override fun getItemCount() = list.size


    class HasilViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var mZonaBackgroundView: LinearLayout? = null
        private var mRankView: TextView? = null
        private var mNameView: TextView? = null
        private var mSkorView: TextView? = null
        private var mZonaView: TextView? = null


        init {
            mZonaBackgroundView = itemView.findViewById(R.id.item_hasil_background_zona)
            mRankView = itemView.findViewById(R.id.item_rank)
            mNameView = itemView.findViewById(R.id.item_name_hasil)
            mSkorView = itemView.findViewById(R.id.item_skor)
            mZonaView = itemView.findViewById(R.id.item_zona)
        }

        fun bind(hasil: HasilData) {
            mRankView?.text = (adapterPosition + 1).toString()
            mNameView?.text = hasil.nama.toString()
            mSkorView?.text = "%15f".format(hasil.skor)
            when (hasil.zona) {
                0 -> {
                    mZonaView?.text = "Tinggi"
                    mZonaBackgroundView?.setBackgroundColor((Color.parseColor("#FF5733")))
                }
                1 -> {
                    mZonaView?.text = "Sedang"
                    mZonaBackgroundView?.setBackgroundColor((Color.parseColor("#FFB74D")))
                }
                2 -> {
                    mZonaView?.text = "Rendah"
                    mZonaBackgroundView?.setBackgroundColor(Color.parseColor("#FFEA00"))
                }
                3 -> {
                    mZonaView?.text = "Tidak Terdampak"
                    mZonaBackgroundView?.setBackgroundColor(Color.GREEN)
                }
            }
        }
    }

}