package com.example.practica_1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.practica_1.data.database.AppDatabase
import com.example.practica_1.navigation.RootNavHost
import com.example.practica_1.repository.ItemRepository
import com.example.practica_1.ui.components.AppTopBar
import com.example.practica_1.ui.components.BottomBar
import com.example.practica_1.ui.components.titleForRoute
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val database = AppDatabase.getDatabase(this)
        val repository = ItemRepository(database.itemDao())

        setContent {
            MaterialTheme {
                val rootNavController = rememberNavController()

                val backStackEntry by rootNavController.currentBackStackEntryAsState()
                val currentRoute = backStackEntry?.destination?.route

                val showBottomBar = when {
                    currentRoute == null -> true
                    currentRoute.startsWith("home_") -> true
                    currentRoute.startsWith("profile_") -> true
                    else -> false
                }

                Scaffold(
                    topBar = {
                        AppTopBar(
                            title = titleForRoute(currentRoute),
                            canNavigateBack =
                                rootNavController.previousBackStackEntry != null,
                            onBack = {
                                rootNavController.popBackStack()
                            }
                        )
                    },
                    bottomBar = {
                        AnimatedVisibility(
                            visible = showBottomBar,
                            enter = slideInHorizontally(
                                initialOffsetX = { it },
                                animationSpec = tween(250)
                            ),
                            exit = slideOutHorizontally(
                                targetOffsetX = { it },
                                animationSpec = tween(250)
                            )
                        ) {
                            BottomBar(rootNavController)
                        }
                    }
                ) { padding ->
                    RootNavHost(
                        navController = rootNavController,
                        repository = repository,
                        modifier = Modifier.padding(padding)
                    )
                }
            }
        }
    }
}







@Composable
fun FormScreen(onBack: () -> Unit) {
    var name by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }

    var submitted by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
            },
            label = {
                Text("Имя")
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
            },
            label = {
                Text("Email")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                submitted = true
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Отправить")
        }

        if (submitted) {
            Text("Отправлено: $name <$email>")
        }

        Spacer(modifier = Modifier.weight(1f))

        OutlinedButton(onClick = onBack) {
            Text("Назад")
        }
    }
}

@Composable
fun GridScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyVerticalGrid(
            modifier = Modifier.weight(1f),
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(20) { index ->
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .height(80.dp)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Карточка ${index + 1}")
                    }
                }
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

@Composable
fun DialogScreen(
    onResult: (Boolean) -> Unit
) {
    var open by remember {
        mutableStateOf(true)
    }

    if (open) {
        AlertDialog(
            onDismissRequest = {
                open = false
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
                        open = false
                        onResult(true)
                    }
                ) {
                    Text("Да")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        open = false
                        onResult(false)
                    }
                ) {
                    Text("Нет")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetScreen(
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val coroutineScope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Bottom Sheet",
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = "Здесь может быть детальная информация или дополнительные действия."
            )

            Button(
                onClick = {
                    coroutineScope.launch {
                        sheetState.hide()
                        onDismiss()
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Закрыть")
            }
        }
    }
}