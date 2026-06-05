package com.example.aplikasigiziku

import android.os.Bundle
import android.widget.Button
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {

    private lateinit var tvKaloriMasuk: TextView
    private lateinit var tvStatusRingkasan: TextView
    lateinit var viewModel: KomunitasViewModel
    lateinit var adapter: PostAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        tvKaloriMasuk     = view.findViewById(R.id.tvKaloriMasuk)
        tvStatusRingkasan = view.findViewById(R.id.tvStatusRingkasan)
        viewModel = ViewModelProvider(requireActivity()).get(KomunitasViewModel::class.java)

        // Ambil nama user dari Firebase → tampilkan di tvUser
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            FirebaseFirestore.getInstance().collection("users").document(uid).get()
                .addOnSuccessListener { doc ->
                    val nama = doc.getString("nama") ?: "Pengguna"
                    view.findViewById<TextView>(R.id.tvUser).text = "Halo, $nama"
                }
        }

        // Setup RecyclerView komunitas langsung di Home
        adapter = PostAdapter(
            emptyList(),
            onLike = { post ->
                if (uid != null) viewModel.toggleLike(post, uid)
            },
            onKomentar = { post ->
                val bundle = Bundle()
                bundle.putString("postId", post.id)
                bundle.putString("postIsi", post.isi)
                findNavController().navigate(R.id.nav_komentar, bundle)
            }
        )

        val rvKomunitas = view.findViewById<RecyclerView>(R.id.rvKomunitas)
        rvKomunitas.layoutManager = LinearLayoutManager(requireContext())
        rvKomunitas.adapter = adapter
        rvKomunitas.isNestedScrollingEnabled = false

        // Sync dan tampilkan postingan
        viewModel.syncPosts()
        viewModel.getAllPosts().observe(viewLifecycleOwner) { adapter.updateData(it) }

        // Navigasi menu
        view.findViewById<LinearLayout>(R.id.menuKalkulator).setOnClickListener {
            findNavController().navigate(R.id.nav_kalkulator)
        }
        view.findViewById<LinearLayout>(R.id.menuVitamin).setOnClickListener {
            findNavController().navigate(R.id.nav_vitamin)
        }
        view.findViewById<LinearLayout>(R.id.menuTips).setOnClickListener {
            findNavController().navigate(R.id.nav_tips)
        }

        // Tombol Profile → ke halaman profil
        view.findViewById<ImageButton>(R.id.btnProfile).setOnClickListener {
            findNavController().navigate(R.id.nav_profil)
        }

        // Tombol Buat Postingan
        val uid2 = FirebaseAuth.getInstance().currentUser?.uid
        FirebaseFirestore.getInstance().collection("users").document(uid2!!).get()
            .addOnSuccessListener { doc ->
                val nama = doc.getString("nama") ?: "Pengguna"
                view.findViewById<Button>(R.id.btnBuatPost).setOnClickListener {
                    val bundle = Bundle()
                    bundle.putString("namaUser", nama)
                    findNavController().navigate(R.id.nav_buat_post, bundle)
                }
            }

        updateRingkasan()
        return view
    }

    override fun onResume() {
        super.onResume()
        updateRingkasan()
    }

    private fun updateRingkasan() {
        val totalKalori = MakananManager.getTotalKalori().toInt()
        val status = when {
            totalKalori < 1000 -> "Kurang"
            totalKalori in 1000..2000 -> "Cukup"
            else -> "Berlebih"
        }
        tvKaloriMasuk.text     = "Kalori Masuk : $totalKalori Kkal"
        tvStatusRingkasan.text = "Status : $status"
    }
}