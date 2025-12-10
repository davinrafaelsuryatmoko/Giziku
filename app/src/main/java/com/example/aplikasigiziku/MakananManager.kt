package com.example.aplikasigiziku

object MakananManager {
    private val listMakanan = mutableListOf<Makanan>()

    fun tambahMakanan(makanan: Makanan) {
        listMakanan.add(makanan)
    }

    fun hapusMakanan(makanan: Makanan) {
        listMakanan.remove(makanan)
    }

    fun getAllMakanan(): List<Makanan> {
        return listMakanan.toList()
    }

    fun getTotalKalori(): Double {
        return listMakanan.sumOf { it.kalori }
    }

    fun getTotalProtein(): Double {
        return listMakanan.sumOf { it.protein }
    }

    fun getTotalLemak(): Double {
        return listMakanan.sumOf { it.lemak }
    }

    fun getTotalKarbohidrat(): Double {
        return listMakanan.sumOf { it.karbohidrat }
    }
}