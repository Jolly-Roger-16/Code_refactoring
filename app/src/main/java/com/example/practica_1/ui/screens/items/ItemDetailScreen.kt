package com.example.practica_1.ui.screens.items

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.practica_1.ui.components.ItemInputDialog
import com.example.practica_1.viewmodel.ItemViewModel

@Composable
fun ItemDetailScreen(
    viewModel: ItemViewModel,
    itemId: Int,
    onBack: () -> Unit
) {
    val items by viewModel.allItems.collectAsState()

    val item = items.find { currentItem ->
        currentItem.id == itemId
    }

    var showEditDialog by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Детали элемента",
            style = MaterialTheme.typography.headlineMedium
        )

        if (item != null) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("ID: ${item.id}")
                    Text("Название: ${item.name}")
                    Text("Описание: ${item.description}")
                    Text("Количество: ${item.quantity}")
                    Text("Цена: ${item.price}")

                    Button(
                        onClick = {
                            viewModel.selectItem(item)
                            showEditDialog = true
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text("Редактировать")
                    }
                }
            }
        } else {
            Text("Элемент не найден")
        }

        Button(onClick = onBack) {
            Text("Назад")
        }
    }

    val selectedItem = viewModel.selectedItem

    if (showEditDialog && selectedItem != null) {
        ItemInputDialog(
            title = "Редактировать элемент",
            initialName = selectedItem.name,
            initialDescription = selectedItem.description,
            initialQuantity = selectedItem.quantity,
            initialPrice = selectedItem.price,
            onDismiss = {
                showEditDialog = false
                viewModel.clearSelectedItem()
            },
            onConfirm = { name, description, quantity, price ->
                viewModel.updateItem(
                    id = selectedItem.id,
                    name = name,
                    description = description,
                    quantity = quantity,
                    price = price
                )

                showEditDialog = false
                viewModel.clearSelectedItem()
            }
        )
    }
}