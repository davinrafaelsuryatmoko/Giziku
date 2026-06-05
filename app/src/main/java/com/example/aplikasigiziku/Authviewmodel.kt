package com.example.aplikasigiziku

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthViewModel : ViewModel() { // KODE UNTUK MEMBUAT VIEWMODEL

    val auth = FirebaseAuth.getInstance() //untuk mengelola akun pengguna
    val db   = FirebaseFirestore.getInstance() //menyimpan data profil

    val pesan    = MutableLiveData<String>()  // mengirim informasi untuk pengguna
    val berhasil = MutableLiveData<Boolean>() // mengirim status berhasil

    fun sudahLogin() = auth.currentUser != null

    fun register(nama: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password) // membuat email password
            .addOnSuccessListener { hasil ->
                val uid = hasil.user!!.uid // mengambil uid
                val data = HashMap<String, String>()
                data["nama"]  = nama
                data["email"] = email
                data["uid"]   = uid
                db.collection("users").document(uid).set(data) // menyimpan data ke firestore
                    .addOnSuccessListener {
                        pesan.value    = "Akun berhasil dibuat!"
                        berhasil.value = true
                    }
            }
            .addOnFailureListener {
                pesan.value    = "Gagal: ${it.message}"
                berhasil.value = false
            }
    }

    fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password) // memverifikadi email dan password
            .addOnSuccessListener {
                pesan.value    = "Login berhasil!"
                berhasil.value = true
            }
            .addOnFailureListener {
                pesan.value    = "Gagal: ${it.message}"
                berhasil.value = false
            }
    }

    fun logout() = auth.signOut() // mengakhiri sesi login
}