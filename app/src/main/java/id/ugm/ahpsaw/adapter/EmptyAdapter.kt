package id.ugm.ahpsaw.adapter

import android.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.marginTop
import androidx.recyclerview.widget.RecyclerView
import id.ugm.ahpsaw.R
import id.ugm.ahpsaw.data.AlternatifData
import id.ugm.ahpsaw.data.KriteriaData

class EmptyAdapter(private val s: String):
        RecyclerView.Adapter<EmptyAdapter.EmptyViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EmptyAdapter.EmptyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val layout = R.layout.recyclerview_empty
        val view = inflater.inflate(layout, parent, false)
        return EmptyAdapter.EmptyViewHolder(view)
    }

    override fun onBindViewHolder(holder: EmptyViewHolder, position: Int) {
        holder.bind(s)
    }
    override fun getItemCount() = 1

    class EmptyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var text: TextView? = null

        init {
            text = itemView.findViewById(R.id.recycler_empty_text)
        }

        fun bind(s: String) {
            text?.text = s
        }
    }


}