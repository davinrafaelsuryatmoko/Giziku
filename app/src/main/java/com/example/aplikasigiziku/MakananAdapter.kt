package com.example.aplikasigiziku

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.graphics.BitmapFactory

class MakananAdapter(
    private val listMakanan: MutableList<Makanan>,
    private val onDeleteClick: (Makanan) -> Unit
) : RecyclerView.Adapter<MakananAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val btnDelete: ImageView = view.findViewById(R.id.btnDeleteItem)
        val ivIcon: ImageView = view.findViewById(R.id.ivMakananIcon)
        val tvNama: TextView = view.findViewById(R.id.tvNamaMakanan)
        val tvKalori: TextView = view.findViewById(R.id.tvKaloriMakanan)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_makanan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val makanan = listMakanan[position]

        holder.tvNama.text = makanan.nama
        holder.tvKalori.text = "${makanan.kalori.toInt()} Kkal"

        // Load foto jika ada
        if (makanan.fotoPath != null) {
            val bitmap = BitmapFactory.decodeFile(makanan.fotoPath)
            holder.ivIcon.setImageBitmap(bitmap)
        } else {
            holder.ivIcon.setImageResource(R.drawable.nasi_item)
        }

        // Button delete
        holder.btnDelete.setOnClickListener {
            onDeleteClick(makanan)
        }
    }

    override fun getItemCount() = listMakanan.size

    fun updateData(newList: List<Makanan>) {
        listMakanan.clear()
        listMakanan.addAll(newList)
        notifyDataSetChanged()
    }
}