package com.example.todoappcompose.navigation.destinations

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.todoappcompose.util.Action
import com.example.todoappcompose.util.Constants
import com.example.todoappcompose.util.Constants.TASK_SCREEN


fun NavGraphBuilder.taskComposable(
    navigateToTaskScreen: (Action) -> Unit
) {
    composable(
        route = TASK_SCREEN,
        arguments = listOf(navArgument(Constants.TASK_ARGUMENT_KEY) {
            type = NavType.IntType
        })
    ) {

    }

}