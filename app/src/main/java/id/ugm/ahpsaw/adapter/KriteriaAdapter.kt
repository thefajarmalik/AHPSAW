package id.ugm.ahpsaw.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.ugm.ahpsaw.R
import id.ugm.ahpsaw.data.KriteriaData

class KriteriaAdapter(private val list: ArrayList<KriteriaData>) :
    RecyclerView.Adapter<KriteriaAdapter.KriteriaViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): KriteriaViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_kriteria, parent, false)
        return KriteriaViewHolder(view)
    }

    override fun onBindViewHolder(holder: KriteriaViewHolder, position: Int) {
        (holder as KriteriaViewHolder).bind(list[position])
    }

    override fun getItemCount() = list.size


    class KriteriaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var mNamaKiriView: TextView? = null
        private var mNamaKananView: TextView? = null


        init {
            mNamaKiriView = itemView.findViewById(R.id.item_kriteria_kiri)
            mNamaKananView = itemView.findViewById(R.id.item_kriteria_kanan)
        }

        fun bind(kriteria: KriteriaData) {
            mNamaKiriView?.text = kriteria.namaKriteria_1.toString()
            mNamaKananView?.text = kriteria.namaKriteria_2.toString()

        }
    }

}