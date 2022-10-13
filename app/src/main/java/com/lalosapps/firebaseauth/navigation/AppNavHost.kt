package com.lalosapps.firebaseauth.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lalosapps.firebaseauth.ui.auth.AuthViewModel
import com.lalosapps.firebaseauth.ui.auth.LoginScreen
import com.lalosapps.firebaseauth.ui.auth.SignupScreen
import com.lalosapps.firebaseauth.ui.home.HomeScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = ROUTE_LOGIN,
    viewModel: AuthViewModel = hiltViewModel()
) {

    val loginFlow = viewModel.loginFlow.collectAsState().value

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(ROUTE_LOGIN) {
            LoginScreen(
                loginFlow = loginFlow,
                onLoginClick = viewModel::login,
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(ROUTE_LOGIN) { inclusive = true }
                    }
                }
            )
        }
        composable(ROUTE_SIGNUP) {
            SignupScreen(
                onSignupClick = {

                },
                onNavigate = {
                    navController.navigate(ROUTE_LOGIN) {
                        popUpTo(ROUTE_SIGNUP) { inclusive = true }
                    }
                }
            )
        }
        composable(ROUTE_HOME) {
            HomeScreen(onLogoutClick = viewModel::logout)
        }
    }
}