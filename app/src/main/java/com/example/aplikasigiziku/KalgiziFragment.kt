package com.example.aplikasigiziku

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class KalgiziFragment : Fragment() {

    private lateinit var rvMakanan: RecyclerView
    private lateinit var adapter: MakananAdapter

    private lateinit var tvTotalKalori: TextView
    private lateinit var tvProtein: TextView
    private lateinit var tvLemak: TextView
    private lateinit var tvKarbohidrat: TextView
    private lateinit var tvStatus: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_kal_gizi, container, false)

        val btnTambahMakanan = view.findViewById<Button>(R.id.btnTambahMakanan)
        val btnCekRiwayat = view.findViewById<Button>(R.id.btnCekRiwayat)
        rvMakanan = view.findViewById(R.id.rvMakanan)

        tvTotalKalori = view.findViewById(R.id.tvTotalKalori)
        tvProtein = view.findViewById(R.id.tvProtein)
        tvLemak = view.findViewById(R.id.tvLemak)
        tvKarbohidrat = view.findViewById(R.id.tvKarbohidrat)
        tvStatus = view.findViewById(R.id.tvStatus)

        setupRecyclerView()

        btnTambahMakanan.setOnClickListener {
            findNavController().navigate(R.id.nav_tambah_makanan)
        }

        btnCekRiwayat.setOnClickListener {
            findNavController().navigate(R.id.nav_home)
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        updateData()
    }

    private fun setupRecyclerView() {
        adapter = MakananAdapter(
            listMakanan = MakananManager.getAllMakanan().toMutableList(),
            onDeleteClick = { makanan ->
                MakananManager.hapusMakanan(makanan)
                updateData()
            }
        )

        rvMakanan.layoutManager = LinearLayoutManager(requireContext())
        rvMakanan.adapter = adapter
    }

    private fun updateData() {
        adapter.updateData(MakananManager.getAllMakanan())

        val totalKalori = MakananManager.getTotalKalori()
        val totalProtein = MakananManager.getTotalProtein()
        val totalLemak = MakananManager.getTotalLemak()
        val totalKarbohidrat = MakananManager.getTotalKarbohidrat()

        tvTotalKalori.text = "${totalKalori.toInt()} Kkal"
        tvProtein.text = "${totalProtein.toInt()} g"
        tvLemak.text = "${totalLemak.toInt()} g"
        tvKarbohidrat.text = "${totalKarbohidrat.toInt()} g"

        tvStatus.text = when {
            totalKalori < 1000 -> "Kurang"
            totalKalori in 1000.0..2000.0 -> "Sesuai Kebutuhan"
            else -> "Berlebih"
        }
    }
}