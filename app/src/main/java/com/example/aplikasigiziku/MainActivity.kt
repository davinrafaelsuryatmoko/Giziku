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


        val btnMasuk = findViewById<Button>(R.id.btnMasuk)
        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)


        btnMasuk.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Username & Password wajib diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Kirim username ke halaman berikutnya
            val intent = Intent(this, ButtonNavigation::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
            finish()
        }


    }
}
