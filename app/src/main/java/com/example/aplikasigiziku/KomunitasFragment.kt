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

class KomunitasFragment : Fragment() {

    lateinit var viewModel: KomunitasViewModel
    lateinit var adapter: PostAdapter
    lateinit var uid: String
    var namaUser = "Pengguna"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_komunitas, container, false)

        uid       = FirebaseAuth.getInstance().currentUser!!.uid
        viewModel = ViewModelProvider(requireActivity()).get(KomunitasViewModel::class.java)

        FirebaseFirestore.getInstance().collection("users").document(uid).get()
            .addOnSuccessListener { namaUser = it.getString("nama") ?: "Pengguna" }

        adapter = PostAdapter(
            emptyList(),
            onLike     = { post -> viewModel.toggleLike(post, uid) },
            onKomentar = { post ->
                val bundle = Bundle()
                bundle.putString("postId", post.id)
                bundle.putString("postIsi", post.isi)
                findNavController().navigate(R.id.nav_komentar, bundle)
            }
        )

        view.findViewById<RecyclerView>(R.id.rvPosts).apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@KomunitasFragment.adapter
        }

        viewModel.syncPosts()
        viewModel.getAllPosts().observe(viewLifecycleOwner) { adapter.updateData(it) }
        viewModel.pesanError.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        view.findViewById<Button>(R.id.btnBuatPost).setOnClickListener {
            val bundle = Bundle()
            bundle.putString("namaUser", namaUser)
            findNavController().navigate(R.id.nav_buat_post, bundle)
        }

        return view
    }
}