package id.ugm.ahpsaw.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        private var mRankView: TextView? = null
        private var mNameView: TextView? = null
        private var mSkorView: TextView? = null


        init {
            mRankView = itemView.findViewById(R.id.item_rank)
            mNameView = itemView.findViewById(R.id.item_name_hasil)
            mSkorView = itemView.findViewById(R.id.item_skor)

        }

        fun bind(hasil: HasilData) {
            mRankView?.text = (adapterPosition+1).toString()
            mNameView?.text = hasil.nama.toString()
            mSkorView?.text = "%15f".format(hasil.skor)

        }
    }

}