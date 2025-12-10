package com.example.aplikasigiziku

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class TipsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tips, container, false)

        val btn1 = view.findViewById<TextView>(R.id.btnSelesai1)
        val btn2 = view.findViewById<TextView>(R.id.btnSelesai2)

        setupToggle(btn1)
        setupToggle(btn2)

        return view
    }

    private fun setupToggle(button: TextView) {
        button.setOnClickListener {
            val selected = !button.isSelected
            button.isSelected = selected

            if (selected) {
                button.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
                button.setBackgroundResource(R.drawable.bg_button_tips_selected)
            } else {
                button.setTextColor(ContextCompat.getColor(requireContext(), R.color.biru))
                button.setBackgroundResource(R.drawable.bg_button_tips_unselected)
            }
        }
    }
}
