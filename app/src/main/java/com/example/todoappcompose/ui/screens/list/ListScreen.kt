package com.example.todoappcompose.ui.screens.list

import android.annotation.SuppressLint
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.todoappcompose.R
import com.example.todoappcompose.ui.theme.fabBackgroundColor
import com.example.todoappcompose.ui.viewModels.SharedViewModel
import com.example.todoappcompose.util.Action
import com.example.todoappcompose.util.Constants.DEFAULT_INT_VALUE
import com.example.todoappcompose.util.SearchAppBarState
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ListScreen(
    navigateToTaskScreen: (taskId: Int) -> Unit,
    sharedViewModel: SharedViewModel
) {
    LaunchedEffect(key1 = true) {
        sharedViewModel.getAllTasks()
    }

    val action by sharedViewModel.action

    val allTasks by sharedViewModel.allTasks.collectAsState()
    val searchAppBarState: SearchAppBarState by sharedViewModel.searchAppBarState
    val searchTextState: String by sharedViewModel.searchTextState

    val scaffoldState = rememberScaffoldState()

    DisplaySnackBar(
        scaffoldState = scaffoldState,
        handleDatabaseActions = {
            sharedViewModel.handleDatabaseActions(action = action)
        },
        onUndoClicked = {
            sharedViewModel.action.value = it
        },
        taskTitle = sharedViewModel.title.value,
        action = action
    )

    Scaffold(
        topBar = {
            ListAppBar(
                sharedViewModel = sharedViewModel,
                searchAppBarState = searchAppBarState,
                searchTextState = searchTextState
            )
        },
        content = {
            ListContent(
                allTasks = allTasks,
                navigateToTaskScreen = navigateToTaskScreen
            )
        },
        floatingActionButton = {
            ListFab(onFabClicked = navigateToTaskScreen)
        },
        scaffoldState = scaffoldState
    )
}

@Composable
fun ListFab(
    onFabClicked: (taskId: Int) -> Unit
) {
    FloatingActionButton(
        onClick = {
            onFabClicked(DEFAULT_INT_VALUE)
        },
        backgroundColor = MaterialTheme.colors.fabBackgroundColor
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(id = R.string.add_button),
            tint = Color.White
        )
    }
}

@Composable
fun DisplaySnackBar(
    scaffoldState: ScaffoldState,
    handleDatabaseActions: () -> Unit,
    onUndoClicked: (Action) -> Unit,
    taskTitle: String,
    action: Action
) {
    handleDatabaseActions()

    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = action) {
        if (action != Action.NO_ACTION) {
            scope.launch {
                val snackBarResult = scaffoldState.snackbarHostState.showSnackbar(
                    message = "${action.name}: $taskTitle",
                    actionLabel = setActionLabel(action = action)
                )
                undoDeleteTask(
                    action = action,
                    snackBarResult = snackBarResult,
                    onUndoClicked = onUndoClicked
                )
            }
        }
    }

}

private fun setActionLabel(action: Action): String {
    return if (action.name == "DELETE") {
        "UNDO"
    } else {
        "OK"
    }
}

private fun undoDeleteTask(
    action: Action,
    snackBarResult: SnackbarResult,
    onUndoClicked: (Action) -> Unit
) {
    if (snackBarResult == SnackbarResult.ActionPerformed && action == Action.DELETE) {
        onUndoClicked(Action.UNDO)
    }
}



