package com.example.listify.ui.theme

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.listify.ui.theme.detail.DetailScreen
import com.example.listify.ui.theme.home.HomeScreen


enum class Routes{
    home,
    detail
}

@Composable
fun NavigationGraph(
    navHostController: NavHostController = rememberNavController()
){
    NavHost(navController = navHostController, startDestination = Routes.home.name) {
        composable(Routes.home.name){
            HomeScreen(onNavigate = {id->
                navHostController.navigate(route = "${Routes.detail.name}?id=$id")
            })
        }
        composable(
            route = "${Routes.detail.name}?id={id}",
            arguments = listOf(navArgument("id"){type = NavType.IntType})
        ){
            val id = it.arguments?.getInt("id")?:-1
            DetailScreen(id = id) {
                navHostController.navigateUp()
            }
        }

    }
}