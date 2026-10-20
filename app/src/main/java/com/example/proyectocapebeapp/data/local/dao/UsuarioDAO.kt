package com.example.proyectocapebeapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.proyectocapebeapp.data.local.entity.UsuarioEntity

@Dao
interface UsuarioDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun registerUser(user: UsuarioEntity)

    @Query("SELECT * FROM users WHERE LOWER  (email) = LOWER  (:email) LIMIT 1")
    suspend fun getUserByEmail(email: String): UsuarioEntity?

    @Query("SELECT * FROM users WHERE id = :usuarioId LIMIT 1")
    suspend fun getUserById(usuarioId: Int): UsuarioEntity?

    @Query("""UPDATE users SET fotoPath = :fotoPath WHERE id = :usuarioId """) suspend fun actualizarFoto( usuarioId: Int,  fotoPath: String)
}