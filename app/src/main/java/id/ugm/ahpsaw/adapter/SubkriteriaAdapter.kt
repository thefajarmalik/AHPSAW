package id.ugm.ahpsaw.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import id.ugm.ahpsaw.R
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
        holder.bind(list[position])
        holder.mReciprocalView?.setOnCheckedChangeListener { _, isChecked ->
            list[position].reciprocal = isChecked
        }

        holder.mPreferensi?.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                // do nothing
            }

            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
                // Do nothing
            }

            override fun afterTextChanged(s: Editable) {
                if (holder.mPreferensi?.text.toString().isNotEmpty()) {
                    list[position].preferensi = holder.mPreferensi?.text.toString().toDouble()
                }
            }

        })
    }

    override fun getItemCount() = list.size


    class SubkriteriaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var mNamaKiriView: TextView? = null
        private var mNamaKananView: TextView? = null
        var mReciprocalView: SwitchCompat? = null
        var mPreferensi: EditText? = null


        init {
            mNamaKiriView = itemView.findViewById(R.id.item_subkriteria_kiri)
            mNamaKananView = itemView.findViewById(R.id.item_subkriteria_kanan)
            mReciprocalView = itemView.findViewById(R.id.item_subkriteria_switch)
            mPreferensi = itemView.findViewById(R.id.item_subkriteria_edittext)
        }

        fun bind(subkriteria: SubkriteriaData) {
            mNamaKiriView?.text = subkriteria.namaSubkriteria_1
            mNamaKananView?.text = subkriteria.namaSubkriteria_2
            mPreferensi?.setText(subkriteria.preferensi.toString())
            mReciprocalView?.isChecked = subkriteria.reciprocal
        }
    }

}