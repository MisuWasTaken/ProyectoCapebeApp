package com.example.proyectocapebeapp.ui.screen

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectocapebeapp.ui.viewmodel.FotoIdentificacionViewModel
@Composable
fun FotoIdentificacionScreen(
    usuarioId: Int,
    onVolver: () -> Unit,
    viewModel: FotoIdentificacionViewModel = viewModel()
) {
    val context =
        LocalContext.current
    val launcherCamara =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts.TakePicturePreview()
        ) { bitmap ->
            if (bitmap != null) {
                viewModel.guardarFoto(
                    context = context,
                    usuarioId = usuarioId,
                    bitmap = bitmap,
                    onSuccess = {
                        Toast.makeText(
                            context,
                            "Foto guardada correctamente.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }
        }
    LaunchedEffect(usuarioId) {
        viewModel.cargarFoto(
            context = context,
            usuarioId = usuarioId
        )
    }
    Column(
        modifier =
            Modifier
                .fillMaxSize(),
        horizontalAlignment =
            Alignment.CenterHorizontally,
        verticalArrangement =
            Arrangement.Center
    ) {
        Text(
            text = "Mi identificacion"
        )
        Spacer(
            modifier =
                Modifier.height(30.dp)
        )
        if (viewModel.fotoBitmap != null) {

            Image(
                bitmap =
                    viewModel.fotoBitmap!!
                        .asImageBitmap(),
                contentDescription =
                    "Foto de identificacion",
                modifier =
                    Modifier
                        .size(200.dp)
                        .clip(CircleShape)
            )
        } else {
            Text(
                text =
                    "Aun no tienes una foto registrada."
            )
        }
        Spacer(
            modifier =
                Modifier.height(30.dp)
        )
        Button(
            onClick = {
                launcherCamara.launch(null)
            },
            modifier =
                Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Tomar foto"
            )
        }
        Spacer(
            modifier =
                Modifier.height(15.dp)
        )
        Button(
            onClick = onVolver,
            modifier =
                Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Volver"
            )
        }
    }
}