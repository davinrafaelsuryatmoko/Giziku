package com.example.aplikasigiziku

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class KalkulatorViewModel(app: Application) : AndroidViewModel(app) {

    val dao = AppDatabase.getInstance(app).makananDao()
    val fs  = FirebaseFirestore.getInstance()
    val hari = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    // Ganti fungsi getMakanan
    fun getMakanan(uid: String): LiveData<List<MakananEntity>> {
        return dao.getAllMakanan(uid)
    }
    fun syncMakananDariFirestore(uid: String) {
        fs.collection("users").document(uid)
            .collection("makanan")
            .get()
            .addOnSuccessListener { result ->
                viewModelScope.launch {
                    for (doc in result) {
                        dao.simpan(MakananEntity(
                            id          = doc.getString("id") ?: return@launch,
                            uid         = doc.getString("uid") ?: "",
                            nama        = doc.getString("nama") ?: "",
                            kategori    = doc.getString("kategori") ?: "",
                            jumlah      = (doc.getLong("jumlah") ?: 1).toInt(),
                            kalori      = doc.getDouble("kalori") ?: 0.0,
                            protein     = doc.getDouble("protein") ?: 0.0,
                            lemak       = doc.getDouble("lemak") ?: 0.0,
                            karbohidrat = doc.getDouble("karbohidrat") ?: 0.0,
                            tanggal     = doc.getString("tanggal") ?: ""
                        ))
                    }
                }
            }
    }

    fun simpan(makanan: MakananEntity) {
        viewModelScope.launch {
            // Simpan ke Room
            dao.simpan(makanan)

            // Simpan ke Firestore
            val data = HashMap<String, Any>()
            data["id"]          = makanan.id
            data["uid"]         = makanan.uid
            data["nama"]        = makanan.nama
            data["kategori"]    = makanan.kategori
            data["jumlah"]      = makanan.jumlah
            data["kalori"]      = makanan.kalori
            data["protein"]     = makanan.protein
            data["lemak"]       = makanan.lemak
            data["karbohidrat"] = makanan.karbohidrat
            data["tanggal"]     = makanan.tanggal

            fs.collection("users").document(makanan.uid)
                .collection("makanan").document(makanan.id)
                .set(data)
        }
    }

    fun hapus(makanan: MakananEntity) {
        viewModelScope.launch {
            // Hapus dari Room
            dao.hapus(makanan)

            // Hapus dari Firestore
            fs.collection("users").document(makanan.uid)
                .collection("makanan").document(makanan.id)
                .delete()
        }
    }
}