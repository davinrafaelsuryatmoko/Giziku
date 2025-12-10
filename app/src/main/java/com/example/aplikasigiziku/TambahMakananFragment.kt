package com.example.aplikasigiziku

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import java.io.File
import java.io.FileOutputStream

class TambahMakananFragment : Fragment() {

    private lateinit var ivPreviewFoto: ImageView
    private lateinit var ivUploadIcon: ImageView
    private var selectedImagePath: String? = null

    // Launcher untuk pick image dari galeri
    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data
            uri?.let {
                handleImageSelection(it)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tambah_makanan, container, false)

        // Ambil reference view
        val etNama = view.findViewById<EditText>(R.id.etNamaMakanan)
        val spinnerKategori = view.findViewById<Spinner>(R.id.spinnerKategori)
        val etJumlah = view.findViewById<EditText>(R.id.etJumlah)
        val etKalori = view.findViewById<EditText>(R.id.etKalori)
        val etProtein = view.findViewById<EditText>(R.id.etProtein)
        val etLemak = view.findViewById<EditText>(R.id.etLemak)
        val etKarbohidrat = view.findViewById<EditText>(R.id.etKarbohidrat)
        val btnSimpan = view.findViewById<Button>(R.id.btnSimpan)
        val btnUploadFoto = view.findViewById<LinearLayout>(R.id.btnUploadFoto)

        ivPreviewFoto = view.findViewById(R.id.ivPreviewFoto)
        ivUploadIcon = view.findViewById(R.id.ivUploadIcon)

        // Setup Spinner Kategori
        val kategoriList = arrayOf("Nasi", "Lauk", "Sayur", "Buah", "Minuman","Makanan", "Lainnya")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, kategoriList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerKategori.adapter = adapter

        // Button Upload Foto
        btnUploadFoto.setOnClickListener {
            openGallery()
        }

        // Button Simpan
        btnSimpan.setOnClickListener {
            val nama = etNama.text.toString()
            val kategori = spinnerKategori.selectedItem.toString()
            val jumlah = etJumlah.text.toString().toIntOrNull() ?: 1
            val kalori = etKalori.text.toString().toDoubleOrNull() ?: 0.0
            val protein = etProtein.text.toString().toDoubleOrNull() ?: 0.0
            val lemak = etLemak.text.toString().toDoubleOrNull() ?: 0.0
            val karbohidrat = etKarbohidrat.text.toString().toDoubleOrNull() ?: 0.0

            // Validasi
            if (nama.isEmpty()) {
                Toast.makeText(requireContext(), "Nama makanan harus diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Buat object Makanan
            val makanan = Makanan(
                nama = nama,
                kategori = kategori,
                jumlah = jumlah,
                kalori = kalori,
                protein = protein,
                lemak = lemak,
                karbohidrat = karbohidrat,
                fotoPath = selectedImagePath
            )

            // Simpan ke MakananManager
            MakananManager.tambahMakanan(makanan)

            // Toast sukses
            Toast.makeText(requireContext(), "Makanan berhasil ditambahkan!", Toast.LENGTH_SHORT).show()

            // Kembali ke Kalkulator Gizi
            findNavController().navigateUp()
        }

        return view
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(intent)
    }

    private fun handleImageSelection(uri: Uri) {
        try {
            // Simpan gambar ke internal storage
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)

            // Compress dan simpan
            val filename = "makanan_${System.currentTimeMillis()}.jpg"
            val file = File(requireContext().filesDir, filename)
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
            outputStream.close()

            // Simpan path
            selectedImagePath = file.absolutePath

            // Tampilkan preview
            ivPreviewFoto.setImageBitmap(bitmap)
            ivPreviewFoto.visibility = View.VISIBLE
            ivUploadIcon.visibility = View.GONE

            Toast.makeText(requireContext(), "Foto berhasil dipilih!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Gagal memuat foto: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}