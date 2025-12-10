package com.example.aplikasigiziku

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Ambil komponen dari layout
        val btnMasuk = findViewById<Button>(R.id.btnMasuk)
        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val txtDaftar = findViewById<TextView>(R.id.txtDaftar)

        // Aksi tombol Masuk
        btnMasuk.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Username & Password wajib diisi!", Toast.LENGTH_SHORT).show()
            } else {
                // Kalau mau pakai validasi sederhana dulu, ini aman:
                val intent = Intent(this, ButtonNavigation::class.java)
                startActivity(intent)
                finish()
            }
        }

        // Aksi klik Daftar
        txtDaftar.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }
}
