package com.example.aplikasigiziku

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfilFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_profil, container, false)

        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return view // uid untuk mengambil dt

        // Ambil data dari Firestore
        FirebaseFirestore.getInstance().collection("users").document(uid).get() // untuk ambil data
            .addOnSuccessListener { doc ->
                view.findViewById<TextView>(R.id.tvNamaProfil).text  = doc.getString("nama")  ?: "-" // menampilkan data deng teks view
                view.findViewById<TextView>(R.id.tvEmailProfil).text = doc.getString("email") ?: "-"
                view.findViewById<TextView>(R.id.tvPhoneProfil).text = doc.getString("phone") ?: "-"
            }

        // Tombol Keluar
        view.findViewById<Button>(R.id.btnKeluar).setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(requireContext(), "Sampai jumpa!", Toast.LENGTH_SHORT).show()
            // Kembali ke Login dan bersihkan back stack
            findNavController().navigate(R.id.nav_login)
        }

        return view
    }
}