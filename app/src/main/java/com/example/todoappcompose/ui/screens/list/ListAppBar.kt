package com.example.todoappcompose.ui.screens.list

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.todoappcompose.R
import com.example.todoappcompose.ui.theme.topAppBarBackgroundColor
import com.example.todoappcompose.ui.theme.topAppBarContentColor

@Composable
fun ListAppBar() {
    DefaultListAppBar()
}

@Preview
@Composable
fun DefaultListAppBar() {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.tasks),
                color = MaterialTheme.colors.topAppBarContentColor
            )
        },
        backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor
    )
}
