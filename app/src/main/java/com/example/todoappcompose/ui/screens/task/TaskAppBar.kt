package com.example.todoappcompose.ui.screens.task

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.todoappcompose.R
import com.example.todoappcompose.components.DisplayAlertDialog
import com.example.todoappcompose.data.models.Priority
import com.example.todoappcompose.data.models.ToDoTask
import com.example.todoappcompose.ui.theme.topAppBarBackgroundColor
import com.example.todoappcompose.ui.theme.topAppBarContentColor
import com.example.todoappcompose.util.Action

@Composable
fun TaskAppBar(
    selectedTask: ToDoTask?,
    navigationToListScreen: (Action) -> Unit
) {
    if (selectedTask == null) {
        NewTaskAppBar(navigationToListScreen = navigationToListScreen)
    } else {
        ExistingTaskAppBar(
            selectedTask = selectedTask,
            navigationToListScreen = navigationToListScreen
        )
    }

}

@Composable
fun NewTaskAppBar(
    navigationToListScreen: (Action) -> Unit,
) {
    TopAppBar(
        navigationIcon = {
            BackAction(
                onBackClicked = navigationToListScreen
            )
        },
        title = {
            Text(
                text = stringResource(R.string.add_task),
                color = MaterialTheme.colors.topAppBarContentColor
            )
        },
        backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor,
        actions = {
            AddAction(
                onAddClicked = navigationToListScreen
            )
        }
    )
}

@Composable
fun ExistingTaskAppBar(
    selectedTask: ToDoTask,
    navigationToListScreen: (Action) -> Unit,
) {
    TopAppBar(
        navigationIcon = {
            CloseAction(
                onCloseClicked = navigationToListScreen
            )
        },
        title = {
            Text(
                text = selectedTask.title,
                color = MaterialTheme.colors.topAppBarContentColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor,
        actions = {
            ExistingTaskAppBarAction(
                selectedTask = selectedTask,
                navigationToListScreen = navigationToListScreen
            )
        }
    )
}

@Composable
fun DeleteAction(
    onDeleteClicked: () -> Unit
) {
    IconButton(onClick = { onDeleteClicked() }) {
        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = stringResource(R.string.delete_icon),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
}


@Composable
fun UpdateAction(
    onUpdateClicked: (Action) -> Unit
) {
    IconButton(onClick = { onUpdateClicked(Action.UPDATE) }) {
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = stringResource(R.string.update_icon),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
}

@Composable
fun CloseAction(
    onCloseClicked: (Action) -> Unit
) {
    IconButton(onClick = { onCloseClicked(Action.NO_ACTION) }) {
        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = stringResource(R.string.close_icon_description),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
}

@Composable
fun BackAction(
    onBackClicked: (Action) -> Unit
) {
    IconButton(onClick = { onBackClicked(Action.NO_ACTION) }) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = stringResource(R.string.back_arrow),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
}

@Composable
fun AddAction(
    onAddClicked: (Action) -> Unit
) {
    IconButton(onClick = { onAddClicked(Action.ADD) }) {
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = stringResource(R.string.add_task),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
}

@Preview
@Composable
fun TaskAppBarPreview() {
    TaskAppBar(
        selectedTask = ToDoTask(
            id = 0,
            title = "title",
            description = "asd",
            priority = Priority.MEDIUM
        ),
        navigationToListScreen = {}
    )
}

@Preview
@Composable
fun ExistingTaskAppBarPreview() {
    ExistingTaskAppBar(
        navigationToListScreen = {},
        selectedTask = ToDoTask(
            id = 0,
            title = "title",
            description = "asd",
            priority = Priority.MEDIUM
        )
    )
}

@Composable
fun ExistingTaskAppBarAction(
    selectedTask: ToDoTask,
    navigationToListScreen: (Action) -> Unit,
) {
    var openDialog by remember {
        mutableStateOf(false)
    }

    DisplayAlertDialog(
        title = stringResource(
            id = R.string.delete_task,
            selectedTask.title
        ),
        message = stringResource(
            id = R.string.delete_task_confirmation,
            selectedTask.title
        ),
        openDialog = openDialog,
        onCloseDialog = {
            openDialog = false
        },
        onYesClicked = {
            navigationToListScreen(Action.DELETE)
        }
    )

    DeleteAction(
        onDeleteClicked = {
            openDialog = true
        }
    )
    UpdateAction(onUpdateClicked = navigationToListScreen)
}
