package com.example.aplikasigiziku

import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class KomentarAdapter(
    private val list: ArrayList<HashMap<String, String>>
) : RecyclerView.Adapter<KomentarAdapter.VH>() {

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val tvNama : TextView = view.findViewById(R.id.tvNamaKomentar)
        val tvTeks : TextView = view.findViewById(R.id.tvTeksKomentar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(LayoutInflater.from(parent.context).inflate(R.layout.item_komentar, parent, false))

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.tvNama.text = list[position]["nama"] ?: ""
        holder.tvTeks.text = list[position]["teks"] ?: ""
    }
}