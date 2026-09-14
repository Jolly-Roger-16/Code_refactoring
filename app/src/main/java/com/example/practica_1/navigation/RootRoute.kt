package com.example.practica_1.navigation


sealed class RootRoute(val route: String) {
    data object List : RootRoute("list")
    data object Detail : RootRoute("detail/{id}")
    data object Form : RootRoute("form")
    data object Grid : RootRoute("grid")
    data object Dynamic : RootRoute("dynamic")

    data object HomeRoot : RootRoute("home_root")
    data object HomeMain : RootRoute("home_main")
    data object HomeInner : RootRoute("home_inner")

    data object ProfileRoot : RootRoute("profile_root")
    data object ProfileMain : RootRoute("profile_main")
    data object ProfileSettings : RootRoute("profile_settings")

    data object Dialog : RootRoute("dialog")
    data object BottomSheet : RootRoute("bottom_sheet")
}