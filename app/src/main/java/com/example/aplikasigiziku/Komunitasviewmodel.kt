package com.example.aplikasigiziku

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import java.util.*

class KomunitasViewModel(app: Application) : AndroidViewModel(app) {

    val dao     = AppDatabase.getInstance(app).postDao()
    val fs      = FirebaseFirestore.getInstance()
    val storage = FirebaseStorage.getInstance()
    val postingBerhasil = MutableLiveData<Boolean>()
    val pesanError      = MutableLiveData<String>()

    fun getAllPosts(): LiveData<List<PostEntity>> = dao.getAllPosts()

    fun syncPosts() {
        fs.collection("komunitas")
            .orderBy("waktu", Query.Direction.DESCENDING)
            .addSnapshotListener { result, error ->
                // Pakai addSnapshotListener bukan .get()
                // Ini realtime — otomatis update setiap ada data baru/lama
                if (error != null || result == null) return@addSnapshotListener
                viewModelScope.launch {
                    for (doc in result) {
                        dao.simpan(PostEntity(
                            id             = doc.getString("id") ?: continue,
                            uid            = doc.getString("uid") ?: "",
                            namaPoster     = doc.getString("namaPoster") ?: "",
                            isi            = doc.getString("isi") ?: "",
                            fotoUrl        = doc.getString("fotoUrl") ?: "",
                            jumlahLike     = (doc.getLong("jumlahLike") ?: 0).toInt(),
                            jumlahKomentar = (doc.getLong("jumlahKomentar") ?: 0).toInt(),
                            waktu          = doc.getLong("waktu") ?: 0
                        ))
                    }
                }
            }
    }

    fun buatPost(uid: String, nama: String, isi: String, fotoUri: Uri?) {
        val id = UUID.randomUUID().toString()
        if (fotoUri != null) {
            val ref = storage.reference.child("foto_komunitas/$id.jpg")
            ref.putFile(fotoUri).addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { url ->
                    simpanKeFirestore(id, uid, nama, isi, url.toString())
                }
            }.addOnFailureListener { pesanError.value = "Gagal upload foto" }
        } else {
            simpanKeFirestore(id, uid, nama, isi, "")
        }
    }

    private fun simpanKeFirestore(id: String, uid: String, nama: String, isi: String, fotoUrl: String) {
        val data = hashMapOf("id" to id, "uid" to uid, "namaPoster" to nama,
            "isi" to isi, "fotoUrl" to fotoUrl, "jumlahLike" to 0,
            "jumlahKomentar" to 0, "waktu" to System.currentTimeMillis())
        fs.collection("komunitas").document(id).set(data)
            .addOnSuccessListener {
                viewModelScope.launch {
                    dao.simpan(PostEntity(id, uid, nama, isi, fotoUrl, 0, 0, System.currentTimeMillis()))
                }
                postingBerhasil.value = true
            }
            .addOnFailureListener { pesanError.value = "Gagal: ${it.message}" }
    }

    fun toggleLike(post: PostEntity, uid: String) {
        val likeRef = fs.collection("komunitas").document(post.id).collection("likes").document(uid)
        likeRef.get().addOnSuccessListener { doc ->
            if (doc.exists()) {
                likeRef.delete()
                fs.collection("komunitas").document(post.id).update("jumlahLike", post.jumlahLike - 1)
                viewModelScope.launch { dao.update(post.copy(jumlahLike = post.jumlahLike - 1)) }
            } else {
                likeRef.set(mapOf("uid" to uid))
                fs.collection("komunitas").document(post.id).update("jumlahLike", post.jumlahLike + 1)
                viewModelScope.launch { dao.update(post.copy(jumlahLike = post.jumlahLike + 1)) }
            }
        }
    }
}