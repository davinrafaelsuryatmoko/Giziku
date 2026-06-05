package com.example.aplikasigiziku

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

// ===== MakananEntity =====
@Entity(tableName = "makanan")
data class MakananEntity(
    @PrimaryKey val id: String,
    val uid: String,
    val nama: String,
    val kategori: String,
    val jumlah: Int,
    val kalori: Double,
    val protein: Double,
    val lemak: Double,
    val karbohidrat: Double,
    val tanggal: String
)

// ===== MakananDao =====
@Dao
interface MakananDao {

    // Ambil SEMUA makanan milik user - tidak peduli tanggal
    @Query("SELECT * FROM makanan WHERE uid = :uid ORDER BY rowid DESC")
    fun getAllMakanan(uid: String): LiveData<List<MakananEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun simpan(makanan: MakananEntity)

    @Delete
    suspend fun hapus(makanan: MakananEntity)
}

// ===== PostEntity =====
@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey val id: String,
    val uid: String,
    val namaPoster: String,
    val isi: String,
    val fotoUrl: String,
    val jumlahLike: Int,
    val jumlahKomentar: Int,
    val waktu: Long
)

// ===== PostDao =====
@Dao
interface PostDao {
    @Query("SELECT * FROM posts ORDER BY waktu DESC")
    fun getAllPosts(): LiveData<List<PostEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun simpan(post: PostEntity)

    @Update
    suspend fun update(post: PostEntity)
}

// ===== AppDatabase =====
@Database(
    entities = [MakananEntity::class, PostEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun makananDao(): MakananDao
    abstract fun postDao(): PostDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java, "db_giziku"
            ).fallbackToDestructiveMigration().build().also { INSTANCE = it }
        }
    }
}