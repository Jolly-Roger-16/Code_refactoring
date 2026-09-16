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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
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