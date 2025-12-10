package com.example.aplikasigiziku

data class Makanan(
    val id: String = System.currentTimeMillis().toString(),
    val nama: String,
    val kategori: String,
    val jumlah: Int,
    val kalori: Double,
    val protein: Double,
    val lemak: Double,
    val karbohidrat: Double,
    val fotoPath: String? = null
)