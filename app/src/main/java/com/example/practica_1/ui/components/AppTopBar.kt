package com.example.practica_1.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,
    canNavigateBack: Boolean,
    onBack: () -> Unit
) {
    TopAppBar(
        title = {
            Text(title)
        },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.List,
                        contentDescription = "Назад"
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Настройки"
                )
            }
        }
    )
}

fun titleForRoute(route: String?): String = when {
    route == null -> "Навигация"
    route.startsWith("list") -> "Список"
    route.startsWith("detail") -> "Детали"
    route.startsWith("form") -> "Форма ввода"
    route.startsWith("grid") -> "Сетка"
    route.startsWith("dynamic") -> "Динамический экран"
    route.startsWith("home_main") -> "Главная"
    route.startsWith("home_inner") -> "Домашний внутренний"
    route.startsWith("profile_main") -> "Профиль"
    route.startsWith("profile_settings") -> "Настройки профиля"
    route.startsWith("dialog") -> "Диалог"
    route.startsWith("bottom_sheet") -> "Bottom Sheet"
    else -> "Приложение"
}