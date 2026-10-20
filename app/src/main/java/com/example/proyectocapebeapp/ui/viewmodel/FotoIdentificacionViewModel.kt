package com.example.proyectocapebeapp.ui.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectocapebeapp.data.database.AppDatabase
import kotlinx.coroutines.launch

class FotoIdentificacionViewModel : ViewModel() {

    var fotoBitmap by mutableStateOf<Bitmap?>(null)
        private set
    fun cargarFoto(
        context: Context,
        usuarioId: Int
    ) {
        viewModelScope.launch {
            val db =
                AppDatabase.getDatabase(context)
            val usuario =
                db.usuarioDao()
                    .getUserById(usuarioId)
            if (usuario?.fotoPath != null) {
                fotoBitmap =
                    BitmapFactory.decodeFile(
                        usuario.fotoPath
                    )
            }
        }
    }
    fun guardarFoto(
        context: Context,
        usuarioId: Int,
        bitmap: Bitmap,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val nombreArchivo =
                "foto_usuario_$usuarioId.jpg"
            val outputStream =
                context.openFileOutput(
                    nombreArchivo,
                    Context.MODE_PRIVATE
                )
            bitmap.compress(
                Bitmap.CompressFormat.JPEG,
                90,
                outputStream
            )
            outputStream.close()
            val fotoPath =
                context.getFileStreamPath(
                    nombreArchivo
                ).absolutePath
            val db =
                AppDatabase.getDatabase(context)
            db.usuarioDao().actualizarFoto(
                usuarioId = usuarioId,
                fotoPath = fotoPath
            )
            fotoBitmap = bitmap
            onSuccess()
        }
    }
}