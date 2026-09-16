package com.example.practica_1.ui.screens.dynamic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DynamicScreen(onBack: () -> Unit) {
    var count by remember {
        mutableStateOf(5)
    }

    val dynamicItems = remember(count) {
        (1..count).map { number ->
            "Элемент №$number"
        }
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
            Button(
                onClick = {
                    if (count < 20) {
                        count++
                    }
                }
            ) {
                Text("+")
            }

            Button(
                onClick = {
                    if (count > 1) {
                        count--
                    }
                }
            ) {
                Text("-")
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(dynamicItems) { text ->
                ListItem(
                    headlineContent = {
                        Text(text)
                    }
                )

                HorizontalDivider()
            }
        }

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text("Назад")
        }
    }
}