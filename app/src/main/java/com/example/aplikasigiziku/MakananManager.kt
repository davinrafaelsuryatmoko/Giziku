package com.example.aplikasigiziku

// Update file MakananManager.kt yang sudah ada
// Tambahkan fungsi setTotalKalori
object MakananManager {

    private var totalKalori: Double = 0.0

    // Fungsi baru - dipanggil dari KalgiziFragment
    fun setTotalKalori(kalori: Double) {
        totalKalori = kalori
    }

    // Fungsi lama - tetap ada
    fun getTotalKalori(): Double {
        return totalKalori
    }
}