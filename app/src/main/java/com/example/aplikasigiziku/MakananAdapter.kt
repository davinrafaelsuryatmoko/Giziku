package com.example.aplikasigiziku

import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MakananAdapter(
    private var list: List<MakananEntity>,
    private val onHapus: (MakananEntity) -> Unit
) : RecyclerView.Adapter<MakananAdapter.VH>() {

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val tvNama   : TextView  = view.findViewById(R.id.tvNamaMakanan)
        val tvKalori : TextView  = view.findViewById(R.id.tvKaloriMakanan)
        val btnHapus : ImageView = view.findViewById(R.id.btnDeleteItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(LayoutInflater.from(parent.context).inflate(R.layout.item_makanan, parent, false))

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = list[position]
        holder.tvNama.text   = item.nama
        holder.tvKalori.text = "${item.kalori.toInt()} Kkal"
        holder.btnHapus.setOnClickListener { onHapus(item) }
    }

    fun updateData(newList: List<MakananEntity>) {
        list = newList
        notifyDataSetChanged()
    }
}