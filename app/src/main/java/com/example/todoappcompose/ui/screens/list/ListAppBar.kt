package com.example.todoappcompose.ui.screens.list

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.ContentAlpha
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.example.todoappcompose.R
import com.example.todoappcompose.components.DisplayAlertDialog
import com.example.todoappcompose.components.PriorityItem
import com.example.todoappcompose.data.models.Priority
import com.example.todoappcompose.ui.theme.LARGE_PADDING
import com.example.todoappcompose.ui.theme.TOP_APP_BAR_HEIGHT
import com.example.todoappcompose.ui.theme.Typography
import com.example.todoappcompose.ui.theme.topAppBarBackgroundColor
import com.example.todoappcompose.ui.theme.topAppBarContentColor
import com.example.todoappcompose.ui.viewModels.SharedViewModel
import com.example.todoappcompose.util.Action
import com.example.todoappcompose.util.SearchAppBarState

@Composable
fun ListAppBar(
    sharedViewModel: SharedViewModel,
    searchAppBarState: SearchAppBarState,
    searchTextState: String
) {
    when (searchAppBarState) {
        SearchAppBarState.CLOSE -> {
            DefaultListAppBar(
                onSearchClicked = {
                    sharedViewModel.searchAppBarState.value = SearchAppBarState.OPEN
                },
                onSortClicked = {
                    sharedViewModel.persistSortState(it)
                },
                onDeleteAllConfirmedClicked = {
                    sharedViewModel.updateAction(newAction = Action.DELETE_ALL)
                }
            )
        }

        else -> {
            SearchAppBar(
                text = searchTextState,
                onTextChange = {
                    sharedViewModel.searchTextState.value = it
                },
                onCloseClicked = {
                    sharedViewModel.searchAppBarState.value = SearchAppBarState.CLOSE
                    sharedViewModel.searchTextState.value = ""
                },
                onSearchClicked = {
                    sharedViewModel.searchTasks(searchQuery = it)
                },
            )
        }


    }

}

@Composable
fun SearchAppBar(
    text: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit,
//    focusRequester: FocusRequester
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(TOP_APP_BAR_HEIGHT),
        elevation = AppBarDefaults.TopAppBarElevation,
        color = MaterialTheme.colors.topAppBarBackgroundColor
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
//                .focusable(true)
//                .focusRequester(focusRequester),
            value = text,
            onValueChange = {
                onTextChange(it)
            },
            placeholder = {
                Text(
                    modifier = Modifier
                        .alpha(ContentAlpha.medium),
                    text = stringResource(R.string.search),
                    color = Color.White
                )
            },
            textStyle = TextStyle(
                color = MaterialTheme.colors.topAppBarContentColor,
                fontSize = MaterialTheme.typography.subtitle1.fontSize
            ),
            singleLine = true,
            leadingIcon = {
                IconButton(
                    modifier = Modifier
                        .alpha(ContentAlpha.disabled),
                    onClick = { }
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(R.string.search_icon_description),
                        tint = MaterialTheme.colors.topAppBarContentColor
                    )

                }
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        if (text.isNotEmpty()) {
                            onTextChange("")
                        } else {
                            onCloseClicked()
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.close_icon_description),
                        tint = MaterialTheme.colors.topAppBarContentColor
                    )

                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearchClicked(text)
                }
            ),
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = MaterialTheme.colors.topAppBarContentColor,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                backgroundColor = Color.Transparent
            )
        )
    }
}

@Composable
fun DefaultListAppBar(
    onSearchClicked: () -> Unit,
    onSortClicked: (Priority) -> Unit,
    onDeleteAllConfirmedClicked: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.tasks),
                color = MaterialTheme.colors.topAppBarContentColor
            )
        },
        actions = {
            ListAppBarActions(
                onSearchClicked,
                onSortClicked,
                onDeleteAllConfirmedClicked
            )
        },
        backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor
    )
}

@Composable
fun ListAppBarActions(
    onSearchClicked: () -> Unit,
    onSortClicked: (Priority) -> Unit,
    onDeleteAllConfirmedClicked: () -> Unit
) {
    var openDialog by remember {
        mutableStateOf(false)
    }

    DisplayAlertDialog(
        title = stringResource(
            id = R.string.delete_all_task
        ),
        message = stringResource(
            id = R.string.delete_all_task_confirmation
        ),
        openDialog = openDialog,
        onCloseDialog = {
            openDialog = false
        },
        onYesClicked = {
            onDeleteAllConfirmedClicked()
        }
    )

    SearchAction(
        onSearchClicked = onSearchClicked
    )
    SortAction(
        onSortClicked = onSortClicked
    )
    DeleteAllAction(
        onDeleteAllConfirmedClicked = {
            openDialog = true
        }
    )
}

@Composable
fun SearchAction(
    onSearchClicked: () -> Unit
) {
    IconButton(
        onClick = { onSearchClicked() }
    ) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = stringResource(R.string.search_tasks),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }

}

@Composable
fun SortAction(
    onSortClicked: (Priority) -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    IconButton(
        onClick = { expanded = true }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_filter),
            contentDescription = stringResource(R.string.sort_tasks),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            Priority.values().slice(setOf(0, 2, 3)).forEach { priority ->
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        onSortClicked(priority)
                    }
                ) {
                    PriorityItem(priority = priority)
                }
            }
        }
    }

}

@Composable
fun DeleteAllAction(
    onDeleteAllConfirmedClicked: () -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    IconButton(
        onClick = { expanded = true }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_more_vert),
            contentDescription = stringResource(R.string.delete_all),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            DropdownMenuItem(
                onClick = {
                    expanded = false
                    onDeleteAllConfirmedClicked()
                }
            ) {
                Text(
                    modifier = Modifier.padding(start = LARGE_PADDING),
                    text = stringResource(R.string.delete_all),
                    style = Typography.subtitle2
                )
            }
        }
    }
}

@Preview
@Composable
private fun DefaultListAppBarPreview() {
    DefaultListAppBar(
        onSearchClicked = {},
        onSortClicked = {},
        onDeleteAllConfirmedClicked = {}
    )
}


@Preview
@Composable
private fun SearchAppBarPreview() {
    SearchAppBar(
        text = "String",
        onTextChange = {},
        onCloseClicked = {},
        onSearchClicked = {},
    )
}

