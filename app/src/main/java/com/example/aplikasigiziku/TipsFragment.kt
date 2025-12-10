package com.example.aplikasigiziku

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class TipsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tips, container, false)

        val btn1 = view.findViewById<Button>(R.id.btnSelesai1)
        val btn2 = view.findViewById<Button>(R.id.btnSelesai2)

        setupToggleButton(btn1)
        setupToggleButton(btn2)

        return view
    }

    private fun setupToggleButton(button: Button) {
        button.setOnClickListener {
            val isSelected = !button.isSelected
            button.isSelected = isSelected

            if (isSelected) {
                button.setTextColor(resources.getColor(android.R.color.white))
            } else {
                button.setTextColor(resources.getColor(R.color.biru))
            }
        }
    }
}
