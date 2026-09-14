package com.example.practica_1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.example.practica_1.data.database.AppDatabase
import com.example.practica_1.repository.ItemRepository
import com.example.practica_1.ui.components.ItemInputDialog
import com.example.practica_1.viewmodel.ItemViewModel
import com.example.practica_1.viewmodel.ItemViewModelFactory
import kotlinx.coroutines.launch
import com.example.practica_1.navigation.RootRoute

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

data class BottomItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

@Composable
fun BottomBar(navController: NavHostController) {
    val bottomItems = listOf(
        BottomItem(
            route = RootRoute.HomeRoot.route,
            label = "Home",
            icon = Icons.Default.Home
        ),
        BottomItem(
            route = RootRoute.ProfileRoot.route,
            label = "Profile",
            icon = Icons.Default.Person
        )
    )

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    NavigationBar {
        bottomItems.forEach { item ->
            NavigationBarItem(
                selected = currentDestination
                    ?.hierarchy
                    ?.any { destination ->
                        destination.route == item.route
                    } == true,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }

                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
                    )
                },
                label = {
                    Text(item.label)
                }
            )
        }
    }
}

@Composable
fun RootNavHost(
    navController: NavHostController,
    repository: ItemRepository,
    modifier: Modifier = Modifier
) {
    val viewModel: ItemViewModel = viewModel(
        factory = ItemViewModelFactory(repository)
    )

    NavHost(
        navController = navController,
        startDestination = RootRoute.HomeRoot.route,
        modifier = modifier,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { it }
            ) + fadeIn()
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -it }
            ) + fadeOut()
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -it }
            ) + fadeIn()
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { it }
            ) + fadeOut()
        }
    ) {
        composable(RootRoute.List.route) {
            ListScreen(
                viewModel = viewModel,
                onItemClick = { id ->
                    navController.navigate("detail/$id")
                },
                onOpenForm = {
                    navController.navigate(RootRoute.Form.route)
                },
                onOpenGrid = {
                    navController.navigate(RootRoute.Grid.route)
                },
                onOpenDynamic = {
                    navController.navigate(RootRoute.Dynamic.route)
                },
                onShowDialog = {
                    navController.navigate(RootRoute.Dialog.route)
                },
                onShowBottomSheet = {
                    navController.navigate(RootRoute.BottomSheet.route)
                }
            )
        }

        composable(
            route = RootRoute.Detail.route,
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "myapp://detail/{id}"
                }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments
                ?.getString("id")
                ?.toIntOrNull()
                ?: 0

            DetailScreen(
                viewModel = viewModel,
                itemId = id,
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(RootRoute.Form.route) {
            FormScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(RootRoute.Grid.route) {
            GridScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(RootRoute.Dynamic.route) {
            DynamicScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        navigation(
            startDestination = RootRoute.HomeMain.route,
            route = RootRoute.HomeRoot.route
        ) {
            composable(RootRoute.HomeMain.route) {
                HomeMainScreen(
                    onOpenInner = {
                        navController.navigate(RootRoute.HomeInner.route)
                    },
                    onOpenList = {
                        navController.navigate(RootRoute.List.route)
                    }
                )
            }

            composable(RootRoute.HomeInner.route) {
                HomeInnerScreen(
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
        }

        navigation(
            startDestination = RootRoute.ProfileMain.route,
            route = RootRoute.ProfileRoot.route
        ) {
            composable(RootRoute.ProfileMain.route) {
                ProfileMainScreen(
                    onOpenSettings = {
                        navController.navigate(RootRoute.ProfileSettings.route)
                    }
                )
            }

            composable(RootRoute.ProfileSettings.route) {
                ProfileSettingsScreen(
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
        }

        composable(RootRoute.Dialog.route) {
            DialogScreen(
                onResult = {
                    navController.popBackStack()
                }
            )
        }

        composable(RootRoute.BottomSheet.route) {
            BottomSheetScreen(
                onDismiss = {
                    navController.popBackStack()
                }
            )
        }
    }
}

@Composable
fun ListScreen(
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

@Composable
fun DetailScreen(
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

@Composable
fun HomeInnerScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Внутренний экран Home")

        Button(onClick = onBack) {
            Text("Назад")
        }
    }
}

@Composable
fun ProfileMainScreen(
    onOpenSettings: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Профиль пользователя",
            style = MaterialTheme.typography.headlineMedium
        )

        Button(onClick = onOpenSettings) {
            Text("Открыть настройки профиля")
        }
    }
}

@Composable
fun ProfileSettingsScreen(onBack: () -> Unit) {
    var notificationsEnabled by remember {
        mutableStateOf(true)
    }

    var darkTheme by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Уведомления",
                modifier = Modifier.weight(1f)
            )

            Switch(
                checked = notificationsEnabled,
                onCheckedChange = {
                    notificationsEnabled = it
                }
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Тёмная тема",
                modifier = Modifier.weight(1f)
            )

            Switch(
                checked = darkTheme,
                onCheckedChange = {
                    darkTheme = it
                }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        OutlinedButton(onClick = onBack) {
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