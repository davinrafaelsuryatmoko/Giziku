package com.example.aplikasigiziku

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class KalgiziFragment : Fragment() {

    lateinit var viewModel: KalkulatorViewModel
    lateinit var adapter: MakananAdapter
    lateinit var uid: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_kal_gizi, container, false)

        uid       = FirebaseAuth.getInstance().currentUser!!.uid
        viewModel = ViewModelProvider(requireActivity()).get(KalkulatorViewModel::class.java)

        // Sync semua data dari Firebase ke Room
        viewModel.syncMakananDariFirestore(uid)

        // Setup RecyclerView
        adapter = MakananAdapter(emptyList()) { makanan ->
            viewModel.hapus(makanan)
        }

        val rv = view.findViewById<RecyclerView>(R.id.rvMakanan)
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter

        // Pantau semua data makanan
        viewModel.getMakanan(uid).observe(viewLifecycleOwner) { list ->
            adapter.updateData(list)

            val kalori  = list.sumOf { it.kalori }
            val protein = list.sumOf { it.protein }
            val lemak   = list.sumOf { it.lemak }
            val karbo   = list.sumOf { it.karbohidrat }

            view.findViewById<TextView>(R.id.tvTotalKalori).text = "${kalori.toInt()} Kkal"
            view.findViewById<TextView>(R.id.tvProtein).text     = "${protein.toInt()} g"
            view.findViewById<TextView>(R.id.tvLemak).text       = "${lemak.toInt()} g"
            view.findViewById<TextView>(R.id.tvKarbohidrat).text = "${karbo.toInt()} g"

            val status = if (kalori < 1200) "Kurang"
            else if (kalori <= 2200) "Sesuai Kebutuhan"
            else "Berlebih"
            view.findViewById<TextView>(R.id.tvStatus).text = status

            MakananManager.setTotalKalori(kalori)
        }

        // Tombol + Makanan
        view.findViewById<Button>(R.id.btnTambahMakanan).setOnClickListener {
            findNavController().navigate(R.id.nav_tambah_makanan)
        }

        return view
    }
}