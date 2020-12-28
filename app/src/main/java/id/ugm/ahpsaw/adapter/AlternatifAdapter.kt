package id.ugm.ahpsaw.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
        (holder as AlternatifViewHolder).bind(list[position], list, position)
        holder.mDeleteButton?.setOnClickListener{
            removeItem(holder)
        }
        holder.mEditButton?.setOnClickListener{
            showUpdateDialog(holder, holder.adapterPosition)
        }

    }

    fun showUpdateDialog(holder: AlternatifViewHolder, pos: Int){
        val mDialogView = LayoutInflater.from(holder.itemView.context).inflate(R.layout.dialog_alternatif_edit, null)
        val mAlertDialogBuilder = androidx.appcompat.app.AlertDialog.Builder(holder.itemView.context)
            .setView(mDialogView)
            .setTitle("Edit Alternatif")
            .setNegativeButton("BATAL") { dialog, id ->
                // Dismiss the dialog
                dialog.dismiss()
            }
            .setPositiveButton("SIMPAN"){dialog, id ->
                var a = ArrayList<Boolean>()
                a.add(mDialogView.findViewById<EditText>(R.id.alternatif_dialog_nama).text.toString().isEmpty())
                a.add(mDialogView.findViewById<EditText>(R.id.alternatif_dialog_positif).text.toString().isEmpty())
                a.add(mDialogView.findViewById<EditText>(R.id.alternatif_dialog_kepadatan).text.toString().isEmpty())
                a.add(mDialogView.findViewById<EditText>(R.id.alternatif_dialog_suhu).text.toString().isEmpty())
                a.add(mDialogView.findViewById<EditText>(R.id.alternatif_dialog_kecepatanangin).text.toString().isEmpty())
                a.add(mDialogView.findViewById<EditText>(R.id.alternatif_dialog_kelembaban).text.toString().isEmpty())
                a.add(mDialogView.findViewById<EditText>(R.id.alternatif_dialog_presipitasi).text.toString().isEmpty())
                a.add(mDialogView.findViewById<EditText>(R.id.alternatif_dialog_tekananudara).text.toString().isEmpty())
                a.add(mDialogView.findViewById<EditText>(R.id.alternatif_dialog_Longitude).text.toString().isEmpty())
                a.add(mDialogView.findViewById<EditText>(R.id.alternatif_dialog_latitude).text.toString().isEmpty())
                a.add(mDialogView.findViewById<EditText>(R.id.alternatif_dialog_ketinggian).text.toString().isEmpty())
                a.add(mDialogView.findViewById<EditText>(R.id.alternatif_dialog_panjangjalan).text.toString().isEmpty())

                val alternatif = AlternatifData(
                    mDialogView.findViewById<EditText>(R.id.alternatif_dialog_nama).text.toString(),
                    mDialogView.findViewById<EditText>(R.id.alternatif_dialog_positif).text.toString().toDouble(),
                    mDialogView.findViewById<EditText>(R.id.alternatif_dialog_kepadatan).text.toString().toDouble(),
                    mDialogView.findViewById<EditText>(R.id.alternatif_dialog_suhu).text.toString().toDouble(),
                    mDialogView.findViewById<EditText>(R.id.alternatif_dialog_kecepatanangin).text.toString().toDouble(),
                    mDialogView.findViewById<EditText>(R.id.alternatif_dialog_kelembaban).text.toString().toDouble(),
                    mDialogView.findViewById<EditText>(R.id.alternatif_dialog_presipitasi).text.toString().toDouble(),
                    mDialogView.findViewById<EditText>(R.id.alternatif_dialog_tekananudara).text.toString().toDouble(),
                    mDialogView.findViewById<EditText>(R.id.alternatif_dialog_Longitude).text.toString().toDouble(),
                    mDialogView.findViewById<EditText>(R.id.alternatif_dialog_latitude).text.toString().toDouble(),
                    mDialogView.findViewById<EditText>(R.id.alternatif_dialog_ketinggian).text.toString().toDouble(),
                    mDialogView.findViewById<EditText>(R.id.alternatif_dialog_panjangjalan).text.toString().toDouble()
                )
                var empty: Boolean =false
                a.forEach {itemIsEmpty ->
                    if (itemIsEmpty){
                        Toast.makeText(holder.itemView.context, "Terdapat data yang kosong! Perubahan tidak disimpan!", Toast.LENGTH_LONG).show()
                        empty = itemIsEmpty
                    }
                }
                if (!empty)
                    editItem(pos, alternatif)
            }
        val mAlertDialog = mAlertDialogBuilder.show()

        mDialogView.findViewById<EditText>(R.id.alternatif_dialog_nama).setText(list[pos].nama)
        mDialogView.findViewById<EditText>(R.id.alternatif_dialog_positif).setText(list[pos].positif.toInt().toString(), TextView.BufferType.EDITABLE)
        mDialogView.findViewById<EditText>(R.id.alternatif_dialog_kepadatan).setText(list[pos].kepadatan.toString(), TextView.BufferType.EDITABLE)
        mDialogView.findViewById<EditText>(R.id.alternatif_dialog_Longitude).setText(list[pos].longitude.toString(), TextView.BufferType.EDITABLE)
        mDialogView.findViewById<EditText>(R.id.alternatif_dialog_latitude).setText(list[pos].latitude.toString(), TextView.BufferType.EDITABLE)
        mDialogView.findViewById<EditText>(R.id.alternatif_dialog_ketinggian).setText(list[pos].ketinggian.toString(), TextView.BufferType.EDITABLE)
        mDialogView.findViewById<EditText>(R.id.alternatif_dialog_panjangjalan).setText(list[pos].panjangJalan.toString(), TextView.BufferType.EDITABLE)
        mDialogView.findViewById<EditText>(R.id.alternatif_dialog_suhu).setText(list[pos].suhu.toString(), TextView.BufferType.EDITABLE)
        mDialogView.findViewById<EditText>(R.id.alternatif_dialog_kecepatanangin).setText(list[pos].kecepatanAngin.toString(), TextView.BufferType.EDITABLE)
        mDialogView.findViewById<EditText>(R.id.alternatif_dialog_kelembaban).setText(list[pos].kelembaban.toString(), TextView.BufferType.EDITABLE)
        mDialogView.findViewById<EditText>(R.id.alternatif_dialog_presipitasi).setText(list[pos].presipitasi.toString(), TextView.BufferType.EDITABLE)
        mDialogView.findViewById<EditText>(R.id.alternatif_dialog_tekananudara).setText(list[pos].tekananUdara.toString(), TextView.BufferType.EDITABLE)
    }

    fun editItem(pos:Int, alternatif: AlternatifData){
        list.set(pos, alternatif)
        notifyDataSetChanged()
    }

    fun removeItem(holder: AlternatifViewHolder){
        val builder = androidx.appcompat.app.AlertDialog.Builder(holder.itemView.context)
        builder.setMessage("Apakah Anda yakin ingin menghapus " + holder.mNameView?.text + "?")
            .setCancelable(true)
            .setPositiveButton("Ya") { dialog, id ->
                list.removeAt(holder.adapterPosition)
                notifyItemRangeChanged(holder.adapterPosition, list.size)
                notifyItemRemoved(holder.adapterPosition)
            }
            .setNegativeButton("Tidak") { dialog, id ->
                // Dismiss the dialog
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }


    override fun getItemCount() = list.size



    class AlternatifViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mNameView: TextView? = null
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
        var mDeleteButton: Button? = null
        var mEditButton: Button? = null


        init {
            mNameView = itemView.findViewById(R.id.item_name_hasil)
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
            mDeleteButton = itemView.findViewById(R.id.item_alternatif_deletebtn)
            mEditButton = itemView.findViewById(R.id.item_alternatif_editbtn)
        }

        fun bind(alternatif: AlternatifData, list: ArrayList<AlternatifData>, pos: Int) {
            mNameView?.text = alternatif.nama
            mPositifView?.text = alternatif.positif.toInt().toString()
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