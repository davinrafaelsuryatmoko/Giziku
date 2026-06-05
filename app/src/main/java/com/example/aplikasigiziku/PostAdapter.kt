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
class PostAdapter(
    private var list: List<PostEntity>,
    private val onLike: (PostEntity) -> Unit,
    private val onKomentar: (PostEntity) -> Unit
) : RecyclerView.Adapter<PostAdapter.VH>() {

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val tvNama     : TextView  = view.findViewById(R.id.tvNamaPoster)
        val tvIsi      : TextView  = view.findViewById(R.id.tvIsiPost)
        val tvLike     : TextView  = view.findViewById(R.id.tvJumlahLike)
        val tvKomentar : TextView  = view.findViewById(R.id.tvJumlahKomentar)
        val imgFoto    : ImageView = view.findViewById(R.id.imgPost)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false))

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val post = list[position]
        holder.tvNama.text     = post.namaPoster
        holder.tvIsi.text      = post.isi
        holder.tvLike.text     = "❤️ ${post.jumlahLike} Suka"
        holder.tvKomentar.text = "💬 ${post.jumlahKomentar} Komentar"

        if (post.fotoUrl.isNotEmpty()) {
            holder.imgFoto.visibility = View.VISIBLE
            Glide.with(holder.itemView.context).load(post.fotoUrl).into(holder.imgFoto)
        } else {
            holder.imgFoto.visibility = View.GONE
        }

        holder.tvLike.setOnClickListener { onLike(post) }
        holder.tvKomentar.setOnClickListener { onKomentar(post) }
    }

    fun updateData(newList: List<PostEntity>) { list = newList; notifyDataSetChanged() }
}