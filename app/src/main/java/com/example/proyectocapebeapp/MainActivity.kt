package com.example.proyectocapebeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.proyectocapebeapp.ui.navigation.AppNavigation
import com.example.proyectocapebeapp.ui.theme.ProyectoCapebeAppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            ProyectoCapebeAppTheme {
                AppNavigation()
            }
        }
    }
}