package id.ugm.ahpsaw.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.ugm.ahpsaw.R
import id.ugm.ahpsaw.data.AlternatifData

class AlternatifAdapter(private val list: ArrayList<AlternatifData>) :
    RecyclerView.Adapter<AlternatifAdapter.AlternatifViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AlternatifViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_alternatif, parent, false)
        return AlternatifViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlternatifViewHolder, position: Int) {
        (holder as AlternatifViewHolder).bind(list[position])
    }

    override fun getItemCount() = list.size


    //class AlternatifViewHolder (inflater: LayoutInflater, parent: ViewGroup) :
    //    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_alternatif, parent, false)) {
    class AlternatifViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var mNameView: TextView? = null
        private var mPositifView: TextView? = null
        private var mKepadatanView: TextView? = null
        private var mLongitudeView: TextView? = null
        private var mLatitudeView: TextView? = null
        private var mKetinggianView: TextView? = null
        private var mPanjangJalanView: TextView? = null
        private var mSuhuView: TextView? = null
        private var mKecepatanAnginView: TextView? = null
        private var mKelembabanView: TextView? = null
        private var mPresipitasiView: TextView? = null
        private var mTekananUdaraView: TextView? = null

        init {
            mNameView = itemView.findViewById(R.id.item_name)
            mPositifView = itemView.findViewById(R.id.item_positif)
            mKepadatanView = itemView.findViewById(R.id.item_kepadatan)
            mLongitudeView = itemView.findViewById(R.id.item_longitude)
            mLatitudeView = itemView.findViewById(R.id.item_latitude)
            mKetinggianView = itemView.findViewById(R.id.item_ketinggian)
            mPanjangJalanView = itemView.findViewById(R.id.item_panjang_jalan)
            mSuhuView = itemView.findViewById(R.id.item_suhu)
            mKecepatanAnginView = itemView.findViewById(R.id.item_kecepatan_angin)
            mKelembabanView = itemView.findViewById(R.id.item_kelembaban)
            mPresipitasiView = itemView.findViewById(R.id.item_presipitasi)
            mTekananUdaraView = itemView.findViewById(R.id.item_tekanan_udara)
        }

        fun bind(alternatif: AlternatifData) {
            mNameView?.text = alternatif.nama
            mPositifView?.text = alternatif.positif.toString()
            mKepadatanView?.text = alternatif.kepadatan.toString()
            mLongitudeView?.text = alternatif.longitude.toString()
            mLatitudeView?.text = alternatif.latitude.toString()
            mKetinggianView?.text = alternatif.ketinggian.toString()
            mPanjangJalanView?.text = alternatif.panjangJalan.toString()
            mSuhuView?.text = alternatif.suhu.toString()
            mKecepatanAnginView?.text = alternatif.kecepatanAngin.toString()
            mKelembabanView?.text = alternatif.kelembaban.toString()
            mPresipitasiView?.text = alternatif.presipitasi.toString()
            mTekananUdaraView?.text = alternatif.tekananUdara.toString()

        }
    }

}