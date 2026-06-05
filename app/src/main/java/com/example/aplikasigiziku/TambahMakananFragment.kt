package com.example.aplikasigiziku

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

class TambahMakananFragment : Fragment() {

    lateinit var viewModel: KalkulatorViewModel
    var fotoUri: Uri? = null

    // Launcher pilih foto


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_tambah_makanan, container, false)

        val uid  = FirebaseAuth.getInstance().currentUser!!.uid
        val hari = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        viewModel = ViewModelProvider(requireActivity()).get(KalkulatorViewModel::class.java)

        // Setup Spinner Kategori
        val kategoriList = listOf("Makanan Pokok", "Lauk Pauk", "Sayuran", "Buah", "Minuman", "Snack")
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, kategoriList)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        view.findViewById<Spinner>(R.id.spinnerKategori).adapter = spinnerAdapter

        // Tombol upload foto — ID dari XML GizuKu


        // Tombol Simpan — ID dari XML GizuKu
        view.findViewById<Button>(R.id.btnSimpan).setOnClickListener {
            val nama     = view.findViewById<EditText>(R.id.etNamaMakanan).text.toString().trim()
            val kategori = view.findViewById<Spinner>(R.id.spinnerKategori).selectedItem.toString()
            val jumlah   = view.findViewById<EditText>(R.id.etJumlah).text.toString().toIntOrNull() ?: 1
            val kalori   = view.findViewById<EditText>(R.id.etKalori).text.toString().toDoubleOrNull() ?: 0.0
            val protein  = view.findViewById<EditText>(R.id.etProtein).text.toString().toDoubleOrNull() ?: 0.0
            val lemak    = view.findViewById<EditText>(R.id.etLemak).text.toString().toDoubleOrNull() ?: 0.0
            val karbo    = view.findViewById<EditText>(R.id.etKarbohidrat).text.toString().toDoubleOrNull() ?: 0.0

            if (nama.isEmpty()) {
                Toast.makeText(requireContext(), "Nama makanan wajib diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val makanan = MakananEntity(
                id          = UUID.randomUUID().toString(),
                uid         = uid,
                nama        = nama,
                kategori    = kategori,
                jumlah      = jumlah,
                kalori      = kalori,
                protein     = protein,
                lemak       = lemak,
                karbohidrat = karbo,
                tanggal     = hari
            )

            viewModel.simpan(makanan)
            Toast.makeText(requireContext(), "✅ $nama berhasil disimpan!", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp() // kembali ke KalgiziFragment
        }

        return view
    }
}