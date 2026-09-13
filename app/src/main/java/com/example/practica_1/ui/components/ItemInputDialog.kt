package com.example.practica_1.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemInputDialog(
    title: String,
    initialName: String = "",
    initialDescription: String = "",
    initialQuantity: Int = 0,
    initialPrice: Double = 0.0,
    onDismiss: () -> Unit,
    onConfirm: (String, String, Int, Double) -> Unit
) {
    var name by remember { mutableStateOf(initialName) }
    var description by remember { mutableStateOf(initialDescription) }
    var quantity by remember { mutableStateOf(initialQuantity.toString()) }
    var price by remember { mutableStateOf(initialPrice.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Название") })
                OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Описание") })
                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it.filter { char -> char.isDigit() } },
                    label = { Text("Количество") }
                )
                OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Цена") })
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val qty = quantity.toIntOrNull() ?: 0
                val prc = price.toDoubleOrNull() ?: 0.0
                if (name.isNotBlank()) {
                    onConfirm(name, description, qty, prc)
                    onDismiss()
                }
            }) { Text("Сохранить") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Отмена") } }
    )
}