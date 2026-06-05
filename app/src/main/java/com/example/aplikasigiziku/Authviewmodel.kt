package com.example.aplikasigiziku

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthViewModel : ViewModel() {

    val auth = FirebaseAuth.getInstance()
    val db   = FirebaseFirestore.getInstance()

    val pesan    = MutableLiveData<String>()
    val berhasil = MutableLiveData<Boolean>()

    fun sudahLogin() = auth.currentUser != null

    fun register(nama: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { hasil ->
                val uid = hasil.user!!.uid
                val data = HashMap<String, String>()
                data["nama"]  = nama
                data["email"] = email
                data["uid"]   = uid
                db.collection("users").document(uid).set(data)
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
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                pesan.value    = "Login berhasil!"
                berhasil.value = true
            }
            .addOnFailureListener {
                pesan.value    = "Gagal: ${it.message}"
                berhasil.value = false
            }
    }

    fun logout() = auth.signOut()
}