package com.example.proyectocapebeapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyectocapebeapp.ui.screen.LoginScreen
import com.example.proyectocapebeapp.ui.screen.RegisterScreen
import com.example.proyectocapebeapp.ui.screen.AdminHomeScreen
import com.example.proyectocapebeapp.ui.screen.InvitadoHomeScreen

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val INVITADO_HOME = "invitado_home"
    const val ADMIN_HOME = "admin_home"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(Routes.REGISTER)
                }
            )
        }
        composable(Routes.REGISTER) {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }
        composable(Routes.INVITADO_HOME) {
            InvitadoHomeScreen()
        }
        composable(Routes.ADMIN_HOME) {
            AdminHomeScreen()
        }
    }
}