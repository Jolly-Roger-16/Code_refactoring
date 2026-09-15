package com.example.practica_1.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeMainScreen(
    onOpenInner: () -> Unit,
    onOpenList: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Домашний экран",
            style = MaterialTheme.typography.headlineMedium
        )

        Button(onClick = onOpenInner) {
            Text("Перейти на внутренний экран Home")
        }

        OutlinedButton(onClick = onOpenList) {
            Text("Перейти к экрану списка")
        }
    }
}