package com.example.proyectocapebeapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyectocapebeapp.ui.screen.LoginScreen
import com.example.proyectocapebeapp.ui.screen.RegisterScreen
import com.example.proyectocapebeapp.ui.screen.AdminHomeScreen
import com.example.proyectocapebeapp.ui.screen.InvitadoHomeScreen
import com.example.proyectocapebeapp.ui.screen.CrearJornadaScreen
import com.example.proyectocapebeapp.ui.screen.VerJornadasScreen
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.proyectocapebeapp.ui.screen.MisVuelosScreen
import com.example.proyectocapebeapp.ui.screen.VerInscritosScreen
import com.example.proyectocapebeapp.ui.screen.VerVuelosProgramadosScreen

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val INVITADO_HOME = "invitado_home"
    const val ADMIN_HOME = "admin_home"
    const val CREAR_JORNADA = "crear_jornada"

    const val VER_JORNADAS = "ver_jornadas"

    const val MIS_VUELOS = "mis_vuelos"

    const val VER_INSCRITOS = "ver_inscritos"

    const val VER_VUELOS_ADMIN = "ver_vuelos_admin"
}
@Composable
fun AppNavigation() {
    var usuarioIdActual by remember { mutableStateOf(0) }
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(Routes.REGISTER)
                },
                onInvitadoSuccess = { usuarioId ->
                    usuarioIdActual = usuarioId
                    navController.navigate(Routes.INVITADO_HOME)
                },
                onAdminSuccess = {
                    navController.navigate(Routes.ADMIN_HOME)
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
            InvitadoHomeScreen(
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onNavigateToVerJornadas = {
                    navController.navigate(Routes.VER_JORNADAS)
                },
                onNavigateToMisVuelos = {
                    navController.navigate(Routes.MIS_VUELOS)
                }
            )
        }
        composable(Routes.VER_JORNADAS) {
            VerJornadasScreen(
                usuarioId = usuarioIdActual,
                onNavigateToInvitado = {
                    navController.popBackStack()
                }
            )
        }
        composable(Routes.ADMIN_HOME) {
            AdminHomeScreen(
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onNavigateToCrearJornada = {
                    navController.navigate(Routes.CREAR_JORNADA)
                },
                onNavigateToVerVuelos = {
                    navController.navigate(Routes.VER_VUELOS_ADMIN)
                },
                onNavigateToVerInscritos = {
                    navController.navigate(Routes.VER_INSCRITOS)
                }
            )
        }
        composable(Routes.CREAR_JORNADA) {
            CrearJornadaScreen(
                onNavigateToAdmin = {
                    navController.popBackStack()
                }
            )
        }
        composable(Routes.VER_INSCRITOS) {
            VerInscritosScreen(
                onNavigateToAdmin = {
                    navController.popBackStack()
                }
            )
        }
        composable(Routes.MIS_VUELOS) {
            MisVuelosScreen(
                usuarioId = usuarioIdActual,
                onNavigateToInvitado = {
                    navController.popBackStack()
                }
            )
        }
        composable(Routes.VER_VUELOS_ADMIN) {
            VerVuelosProgramadosScreen(
                onNavigateToAdmin = {
                    navController.popBackStack()
                }
            )
        }
    }
}