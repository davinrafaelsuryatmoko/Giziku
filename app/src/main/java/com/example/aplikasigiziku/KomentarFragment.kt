package com.example.aplikasigiziku

import android.os.Bundle
import android.net.Uri

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

class KomentarFragment : Fragment() {

    val daftarKomentar = ArrayList<HashMap<String, String>>()
    lateinit var adapter: KomentarAdapter
    lateinit var postId: String
    var namaUser = "Pengguna"
    val fs = FirebaseFirestore.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_komentar, container, false)

        postId = arguments?.getString("postId") ?: return view
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        view.findViewById<TextView>(R.id.tvIsiPostKomentar).text = arguments?.getString("postIsi") ?: ""

        fs.collection("users").document(uid).get()
            .addOnSuccessListener { namaUser = it.getString("nama") ?: "Pengguna" }

        adapter = KomentarAdapter(daftarKomentar)
        val rv = view.findViewById<RecyclerView>(R.id.rvKomentar)
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter

        fs.collection("komunitas").document(postId).collection("komentar")
            .orderBy("waktu", Query.Direction.ASCENDING)
            .addSnapshotListener { result, _ ->
                if (result != null) {
                    daftarKomentar.clear()
                    for (doc in result) {
                        daftarKomentar.add(hashMapOf(
                            "nama" to (doc.getString("nama") ?: ""),
                            "teks" to (doc.getString("teks") ?: "")
                        ))
                    }
                    adapter.notifyDataSetChanged()
                    if (daftarKomentar.isNotEmpty()) rv.scrollToPosition(daftarKomentar.size - 1)
                }
            }

        view.findViewById<TextView>(R.id.btnKirimKomentar).setOnClickListener {
            val teks = view.findViewById<EditText>(R.id.etKomentar).text.toString().trim()
            if (teks.isEmpty()) return@setOnClickListener

            fs.collection("komunitas").document(postId).collection("komentar")
                .add(hashMapOf("nama" to namaUser, "teks" to teks, "uid" to uid, "waktu" to System.currentTimeMillis()))
                .addOnSuccessListener {
                    fs.collection("komunitas").document(postId).get().addOnSuccessListener { doc ->
                        val jumlah = (doc.getLong("jumlahKomentar") ?: 0) + 1
                        fs.collection("komunitas").document(postId).update("jumlahKomentar", jumlah)
                    }
                    view.findViewById<EditText>(R.id.etKomentar).setText("")
                }
        }

        return view
    }
}