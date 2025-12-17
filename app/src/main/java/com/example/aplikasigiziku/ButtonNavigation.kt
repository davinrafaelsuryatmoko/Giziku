package com.example.aplikasigiziku

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class ButtonNavigation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_button_navigation)

        val navBottom = findViewById<BottomNavigationView>(R.id.nav_bottom) //ambil tombol yang ada di bawah

        val navController = supportFragmentManager // lemari besar yang berisi banyak fragment untuk mencari fragment dan menampilkan fragment
            .findFragmentById(R.id.nav_host_fragment)!! //carilah fragment yang id nya
            .findNavController() //akses untuk pindah halaman /fragment
//findNavControler.navigate()
        val appBarConfiguration = AppBarConfiguration.Builder(
            R.id.nav_home, R.id.nav_kalkulator, R.id.nav_vitamin, R.id.nav_tips
        ).build()

//        setupActionBarWithNavController(navController, appBarConfiguration)
        navBottom.setupWithNavController(navController) //menghubungkan tombol bawah button nafigationview dengan navcontroler tombol ditekan navcontoler memindahkan fragment yang sesuai

        navBottom.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    navController.navigate(R.id.nav_home)
                    true
                }
                R.id.nav_kalkulator -> {
                    navController.navigate(R.id.nav_kalkulator)
                    true
                }
                R.id.nav_vitamin -> {
                    navController.navigate(R.id.nav_vitamin)
                    true
                }
                R.id.nav_tips -> {
                    navController.navigate(R.id.nav_tips)
                    true
                }
                else -> false
            }
        }
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
    }
}