package com.example.practica_1.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navDeepLink
import com.example.practica_1.BottomSheetScreen
import com.example.practica_1.DialogScreen
import com.example.practica_1.DynamicScreen
import com.example.practica_1.ui.screens.form.FormScreen
import com.example.practica_1.GridScreen
import com.example.practica_1.ui.screens.profile.ProfileMainScreen
import com.example.practica_1.ui.screens.profile.ProfileSettingsScreen
import com.example.practica_1.repository.ItemRepository
import com.example.practica_1.ui.screens.home.HomeInnerScreen
import com.example.practica_1.ui.screens.home.HomeMainScreen
import com.example.practica_1.ui.screens.items.ItemDetailScreen
import com.example.practica_1.ui.screens.items.ItemListScreen
import com.example.practica_1.viewmodel.ItemViewModel
import com.example.practica_1.viewmodel.ItemViewModelFactory

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
            ItemListScreen(
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

            ItemDetailScreen(
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