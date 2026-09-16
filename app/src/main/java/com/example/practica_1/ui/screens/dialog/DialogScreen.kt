package com.example.practica_1.ui.screens.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun DialogScreen(
    onResult: (Boolean) -> Unit
) {
    var open = remember {
        mutableStateOf(true)
    }

    if (open.value) {
        AlertDialog(
            onDismissRequest = {
                open.value = false
                onResult(false)
            },
            title = {
                Text("Подтверждение")
            },
            text = {
                Text("Принять условия?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        open.value = false
                        onResult(true)
                    }
                ) {
                    Text("Да")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        open.value = false
                        onResult(false)
                    }
                ) {
                    Text("Нет")
                }
            }
        )
    }
}