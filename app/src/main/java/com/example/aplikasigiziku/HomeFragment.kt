package com.example.aplikasigiziku

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class HomeFragment : Fragment() {

    private lateinit var tvKaloriMasuk: TextView
    private lateinit var tvStatusRingkasan: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        tvKaloriMasuk = view.findViewById(R.id.tvKaloriMasuk)
        tvStatusRingkasan = view.findViewById(R.id.tvStatusRingkasan)

        val btnKalkulator = view.findViewById<LinearLayout>(R.id.menuKalkulator)
        val btnVitamin = view.findViewById<LinearLayout>(R.id.menuVitamin)
        val btnTips = view.findViewById<LinearLayout>(R.id.menuTips)

        btnKalkulator.setOnClickListener {
            findNavController().navigate(R.id.nav_kalkulator)
        }

        btnVitamin.setOnClickListener {
            findNavController().navigate(R.id.nav_vitamin)
        }

        btnTips.setOnClickListener {
            findNavController().navigate(R.id.nav_tips)
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

        tvKaloriMasuk.text = "Kalori Masuk : $totalKalori Kkal"
        tvStatusRingkasan.text = "Status : $status"
    }
}