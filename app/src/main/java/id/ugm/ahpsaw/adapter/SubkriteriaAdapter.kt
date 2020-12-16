package id.ugm.ahpsaw.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.ugm.ahpsaw.R
import id.ugm.ahpsaw.data.KriteriaData
import id.ugm.ahpsaw.data.SubkriteriaData

class SubkriteriaAdapter(private val list: ArrayList<SubkriteriaData>) :
    RecyclerView.Adapter<SubkriteriaAdapter.SubkriteriaViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SubkriteriaViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_subkriteria, parent, false)
        return SubkriteriaViewHolder(view)
    }

    override fun onBindViewHolder(holder: SubkriteriaViewHolder, position: Int) {
        (holder as SubkriteriaViewHolder).bind(list[position])
    }

    override fun getItemCount() = list.size


    class SubkriteriaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var mNamaKiriView: TextView? = null
        private var mNamaKananView: TextView? = null


        init {
            mNamaKiriView = itemView.findViewById(R.id.item_subkriteria_kiri)
            mNamaKananView = itemView.findViewById(R.id.item_subkriteria_kanan)
        }

        fun bind(list: SubkriteriaData) {
            mNamaKiriView?.text = list.namaSubkriteria_1.toString()
            mNamaKananView?.text = list.namaSubkriteria_2.toString()

        }
    }

}