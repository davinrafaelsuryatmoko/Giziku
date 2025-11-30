package com.example.aplikasigiziku

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val btnKalkulator = view.findViewById<LinearLayout>(R.id.menuKalkulator)
        val btnVitamin = view.findViewById<LinearLayout>(R.id.menuVitamin)
        val btnTips = view.findViewById<LinearLayout>(R.id.menuTips)

        val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.nav_bottom)

        // ✅ Langsung ke DESTINATION, bukan ACTION
        btnKalkulator.setOnClickListener {
            bottomNav.selectedItemId = R.id.nav_kalkulator
        }

        btnVitamin.setOnClickListener {
            bottomNav.selectedItemId = R.id.nav_vitamin
        }

        btnTips.setOnClickListener {
            bottomNav.selectedItemId = R.id.nav_tips
        }

        return view
    }
}