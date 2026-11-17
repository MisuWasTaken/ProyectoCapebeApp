package com.example.proyectocapebeapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.proyectocapebeapp.data.local.entity.UsuarioEntity

@Dao
interface UsuarioDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun registerUser(user: UsuarioEntity): Long

    @Query("SELECT * FROM users WHERE LOWER(email) = LOWER(:email) LIMIT 1")
    suspend fun getUserByEmail(email: String): UsuarioEntity?

    @Query("SELECT * FROM users WHERE id = :usuarioId LIMIT 1")
    suspend fun getUserById(usuarioId: Int): UsuarioEntity?

    @Query("SELECT * FROM users WHERE supabaseId = :supabaseId LIMIT 1")
    suspend fun getUserBySupabaseId(supabaseId: String): UsuarioEntity?

    @Query("UPDATE users SET name = :nombre, email = :email, rol = :rol, supabaseId = :supabaseId, password = '' WHERE id = :usuarioId")
    suspend fun actualizarCuenta(
        usuarioId: Int,
        nombre: String,
        email: String,
        rol: String,
        supabaseId: String
    )

    @Query("UPDATE users SET fotoPath = :fotoPath WHERE id = :usuarioId")
    suspend fun actualizarFoto(usuarioId: Int, fotoPath: String)
}
