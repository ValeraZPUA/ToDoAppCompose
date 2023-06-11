package com.example.todoappcompose.components

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.todoappcompose.R

@Composable
fun DisplayAlertDialog(
    title: String,
    message: String,
    openDialog: Boolean,
    onCloseDialog: () -> Unit,
    onYesClicked: () -> Unit
) {
    if (openDialog) {
        AlertDialog(
            title = {
                Text(
                    text = title,
                    fontSize = MaterialTheme.typography.h5.fontSize,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = message,
                    fontSize = MaterialTheme.typography.subtitle1.fontSize,
                    fontWeight = FontWeight.Normal
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onYesClicked()
                        onCloseDialog()
                    }
                ) {
                    Text(
                        text = stringResource(R.string.yes)
                    )
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        onCloseDialog()
                    }
                ) {
                    Text(
                        text = stringResource(R.string.no)
                    )
                }
            },
            onDismissRequest = {
                onCloseDialog()
            }
        )
    }
}
