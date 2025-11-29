package com.example.aplikasigiziku

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val btnKalkulator = view.findViewById<Button>(R.id.btnKalkulator)
        val btnVitamin = view.findViewById<Button>(R.id.btnVitamin)
        val btnTips = view.findViewById<Button>(R.id.btnTips)

        // ✅ Langsung ke DESTINATION, bukan ACTION
        btnKalkulator.setOnClickListener {
            findNavController().navigate(R.id.nav_kalkulator)
        }

        btnVitamin.setOnClickListener {
            findNavController().navigate(R.id.nav_vitamin)
        }

        btnTips.setOnClickListener {
            findNavController().navigate(R.id.nav_tips)
        }

        return view
    }
}
