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

class LoginFragment : Fragment() {

    lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.activity_welcome, container, false)

        viewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)

        // Reset status biar tidak auto navigasi setelah logout
        viewModel.berhasil.value = false

        // Cek apakah masih login - hanya redirect kalau memang masih ada sesi aktif
        if (viewModel.auth.currentUser != null) {
            findNavController().navigate(R.id.nav_home)
            return view
        }

        val etEmail    = view.findViewById<EditText>(R.id.etUsername)
        val etPassword = view.findViewById<EditText>(R.id.etPassword)
        val btnMasuk   = view.findViewById<Button>(R.id.btnMasuk)
        val tvDaftar   = view.findViewById<TextView>(R.id.txtDaftar)

        viewModel.pesan.observe(viewLifecycleOwner) { pesan ->
            Toast.makeText(requireContext(), pesan, Toast.LENGTH_SHORT).show()
        }

        viewModel.berhasil.observe(viewLifecycleOwner) { berhasil ->
            if (berhasil == true) {
                findNavController().navigate(R.id.nav_home)
            }
        }

        btnMasuk.setOnClickListener {
            val email    = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Email dan password wajib diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.login(email, password)
        }

        tvDaftar.setOnClickListener {
            findNavController().navigate(R.id.nav_register)
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        // Reset status login setiap kali fragment ini muncul
        viewModel.berhasil.value = false
    }
}