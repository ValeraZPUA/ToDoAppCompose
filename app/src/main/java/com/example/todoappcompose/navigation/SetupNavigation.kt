package com.example.todoappcompose.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.todoappcompose.navigation.destinations.listComposable
import com.example.todoappcompose.navigation.destinations.splashComposable
import com.example.todoappcompose.navigation.destinations.taskComposable
import com.example.todoappcompose.ui.viewModels.SharedViewModel
import com.example.todoappcompose.util.Constants
import com.example.todoappcompose.util.Constants.LIST_SCREEN

@Composable
fun SetupNavigation(
    navController: NavHostController,
    sharedViewModel: SharedViewModel
) {

    val screen = remember(navController) {
        Screens(
            navController = navController
        )
    }

    NavHost(
        navController = navController,
        startDestination = Constants.SPLASH_SCREEN
    ) {
        splashComposable(
            navigateToListScreen = screen.splash
        )
        listComposable(
            navigateToTaskScreen = screen.list,
            sharedViewModel = sharedViewModel
        )
        taskComposable(
            navigateToListScreen = screen.task,
            sharedViewModel = sharedViewModel
        )
    }

}