package com.example.aplikasigiziku

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

class RegisterFragment : Fragment() {

    lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.activity_register, container, false)

        viewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)

        val etNama       = view.findViewById<EditText>(R.id.etNama)
        val etEmail      = view.findViewById<EditText>(R.id.etEmail)
        val etPassword   = view.findViewById<EditText>(R.id.etPassword)
        val etKonfirmasi = view.findViewById<EditText>(R.id.etKonfirmasi)
        val btnDaftar    = view.findViewById<Button>(R.id.btnDaftar)
        val tvMasuk      = view.findViewById<TextView>(R.id.txtMasuk)

        viewModel.pesan.observe(viewLifecycleOwner) { pesan ->
            Toast.makeText(requireContext(), pesan, Toast.LENGTH_SHORT).show()
        }

        viewModel.berhasil.observe(viewLifecycleOwner) { berhasil ->
            if (berhasil) findNavController().navigate(R.id.nav_login)
        }

        btnDaftar.setOnClickListener {
            val nama      = etNama.text.toString().trim()
            val email     = etEmail.text.toString().trim()
            val password  = etPassword.text.toString().trim()
            val konfirmasi = etKonfirmasi.text.toString().trim()

            if (nama.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Semua kolom wajib diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password != konfirmasi) {
                Toast.makeText(requireContext(), "Password tidak sama!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.length < 6) {
                Toast.makeText(requireContext(), "Password minimal 6 karakter!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.register(nama, email, password)
        }

        tvMasuk.setOnClickListener {
            findNavController().navigate(R.id.nav_login)
        }

        return view
    }
}