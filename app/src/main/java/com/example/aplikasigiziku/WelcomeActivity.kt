package com.example.aplikasigiziku

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val tombol = findViewById<Button>(R.id.button1)

        tombol.setOnClickListener {
            val intent = Intent(this, ButtonNavigation::class.java)
            startActivity(intent)
        }
    }
}
