package com.brewflow.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.brewflow.presentation.home.HomeViewModel
import com.brewflow.ui.screens.BrewScreen
import com.brewflow.ui.screens.HomeScreen
import com.brewflow.ui.screens.MethodDetailScreen
import com.brewflow.ui.screens.SplashScreen
import com.brewflow.ui.theme.BrewFlowTheme

@Composable
fun BrewFlowAppUI() {
    BrewFlowTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "splash") {
                composable("splash") {
                    SplashScreen(onTimeout = {
                        navController.navigate("home") {
                            popUpTo("splash") { inclusive = true }
                        }
                    })
                }
                composable("home") {
                    val vm: HomeViewModel = hiltViewModel()
                    HomeScreen(
                        viewModel = vm,
                        onOpenMethod = { id ->
                            navController.navigate("method/$id")
                        }
                    )
                }
                composable(
                    "method/{methodId}",
                    arguments = listOf(navArgument("methodId") { type = NavType.StringType })
                ) { backStack ->
                    val methodId = backStack.arguments?.getString("methodId")!!
                    val vm: HomeViewModel = hiltViewModel()
                    val methods = vm.methods.collectAsState().value
                    val method = methods.firstOrNull { it.id == methodId }
                    if (method != null) {
                        MethodDetailScreen(
                            method = method,
                            onStartBrew = { id, cups ->
                                navController.navigate("brew/$id/$cups")
                            },
                            onBack = { navController.popBackStack() }
                        )
                    } else {
                        // simple fallback
                    }
                }

                composable(
                    "brew/{methodId}/{cups}",
                    arguments = listOf(
                        navArgument("methodId") { type = NavType.StringType },
                        navArgument("cups") { type = NavType.IntType }
                    )
                ) { backStack ->
                    val methodId = backStack.arguments?.getString("methodId")!!
                    val cups = backStack.arguments?.getInt("cups") ?: 1
                    val vm: HomeViewModel = hiltViewModel()
                    val methods = vm.methods.collectAsState().value
                    val method = methods.firstOrNull { it.id == methodId }
                    if (method != null) {
                        BrewScreen(
                            method = method,
                            cups = cups,
                            onFinish = { navController.popBackStack("home", false) }
                        )
                    }
                }
            }
        }
    }
}
