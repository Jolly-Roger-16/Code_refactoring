package com.example.practica_1.ui.screens.items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedButton
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
fun ItemListScreen(
    viewModel: ItemViewModel,
    onItemClick: (Int) -> Unit,
    onOpenForm: () -> Unit,
    onOpenGrid: () -> Unit,
    onOpenDynamic: () -> Unit,
    onShowDialog: () -> Unit,
    onShowBottomSheet: () -> Unit
) {
    val items by viewModel.allItems.collectAsState()

    var showAddDialog by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = onOpenForm) {
                Text("Форма")
            }

            Button(onClick = onOpenGrid) {
                Text("Сетка")
            }

            Button(onClick = onOpenDynamic) {
                Text("Динамика")
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedButton(onClick = onShowDialog) {
                Text("Диалог")
            }

            OutlinedButton(onClick = onShowBottomSheet) {
                Text("BottomSheet")
            }
        }

        Button(
            onClick = {
                showAddDialog = true
            },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text("Добавить элемент")
        }

        if (items.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text("Список пока пуст")
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(
                    items = items,
                    key = { item -> item.id }
                ) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 16.dp,
                                vertical = 4.dp
                            )
                            .clickable {
                                onItemClick(item.id)
                            }
                    ) {
                        ListItem(
                            headlineContent = {
                                Text(item.name)
                            },
                            supportingContent = {
                                Text(
                                    text = "Описание: ${item.description}\n" +
                                            "Количество: ${item.quantity}, цена: ${item.price}"
                                )
                            },
                            trailingContent = {
                                IconButton(
                                    onClick = {
                                        viewModel.deleteItem(item)
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Удалить элемент"
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        ItemInputDialog(
            title = "Новый элемент",
            onDismiss = {
                showAddDialog = false
            },
            onConfirm = { name, description, quantity, price ->
                viewModel.insertItem(
                    name = name,
                    description = description,
                    quantity = quantity,
                    price = price
                )

                showAddDialog = false
            }
        )
    }
}