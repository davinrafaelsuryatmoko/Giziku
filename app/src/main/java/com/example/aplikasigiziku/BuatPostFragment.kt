package com.example.aplikasigiziku

import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth

class BuatPostFragment : Fragment() {

    lateinit var viewModel: KomunitasViewModel
    var fotoUri: Uri? = null

    val pilihFoto = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            fotoUri = uri
            view?.findViewById<ImageView>(R.id.imgPreviewPost)?.apply {
                setImageURI(uri)
                visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_buat_post, container, false)

        val uid      = FirebaseAuth.getInstance().currentUser!!.uid
        val namaUser = arguments?.getString("namaUser") ?: "Pengguna"
        viewModel    = ViewModelProvider(requireActivity()).get(KomunitasViewModel::class.java)

        // Aktifkan tombol back ← di toolbar
        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Buat Postingan"
        }

        viewModel.postingBerhasil.observe(viewLifecycleOwner) {
            if (it) {
                Toast.makeText(requireContext(), "✅ Berhasil diposting!", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        }
        viewModel.pesanError.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        view.findViewById<Button>(R.id.btnPilihFoto).setOnClickListener {
            pilihFoto.launch("image/*")
        }

        view.findViewById<Button>(R.id.btnPosting).setOnClickListener {
            val isi = view.findViewById<EditText>(R.id.etIsiPost).text.toString().trim()
            if (isi.isEmpty()) {
                Toast.makeText(requireContext(), "Tulis sesuatu dulu!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.buatPost(uid, namaUser, isi, fotoUri)
        }

        return view
    }

    // Tombol back ← di toolbar
    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            findNavController().navigateUp()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }
}