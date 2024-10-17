package com.example.whatsappsaver.navigation

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.whatsappsaver.Screens.PicDetail
import com.example.whatsappsaver.Screens.Privacy_Policy
import com.example.whatsappsaver.Screens.Saved
import com.example.whatsappsaver.Screens.SettingScreen
import com.example.whatsappsaver.Screens.TermsConditionScreen
import com.example.whatsappsaver.Screens.VideoDetail
import com.example.whatsappsaver.Screens.Videos
import com.example.whatsappsaver.Screens.WhatsAppStatusScreen

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun Navigation(
    navController: NavHostController,
    selectedLanguage: String,
    onLanguageChanged: (String) -> Unit
) {
    NavHost(navController = navController, startDestination = Screens.ImagesScreen.route) {
        composable(Screens.ImagesScreen.route) {
            WhatsAppStatusScreen(navController, selectedLanguage)
        }
        composable(Screens.VideosScreen.route) {
            Videos(navController = navController, selectedLanguage)
        }
        composable(Screens.Saved.route) {
            Saved(navController = navController, selectedLanguage)
        }
        composable(Screens.SettingScreen.route) {
            SettingScreen(
                navController = navController,
                selectedLanguage = selectedLanguage,
                onLanguageChanged = onLanguageChanged
            )
        }
        composable(Screens.VideoDetail.route + "/{videoPath}") { backStackEntry ->
            val videoPath = backStackEntry.arguments?.getString("videoPath")
            if (videoPath != null) {
                VideoDetail(navController = navController, videoPath, selectedLanguage)
            }
        }

        composable(Screens.PicDetail.route + "/{PicPath}") { backStackEntry ->
            val PicPath = backStackEntry.arguments?.getString("PicPath")
            if (PicPath != null) {
                PicDetail(navController = navController, PicPath, selectedLanguage)
            }
        }

        composable(Screens.Privacy_Policy.route) {
            Privacy_Policy(navController = navController)
        }
        composable(Screens.TermsScreen.route) {
            TermsConditionScreen(navController)
        }
    }
}


sealed class Screens(
    val route: String,
    val selectedIcon: ImageVector,
    val unSelectedIcon: ImageVector
) {
    object ImagesScreen : Screens(
        "Images", Icons.Filled.Person, Icons.Outlined.Person
    )

    object VideosScreen : Screens(
        "Videos", Icons.Filled.PlayArrow, Icons.Outlined.PlayArrow
    )

    object Saved : Screens(
        "Saved", Icons.Filled.ExitToApp, Icons.Outlined.ExitToApp
    )

    object SettingScreen : Screens(
        "Setting", Icons.Filled.Settings, Icons.Outlined.Settings
    )

    object VideoDetail : Screens(
        "VideoDetail", Icons.Filled.Settings, Icons.Outlined.Settings
    )

    object PicDetail : Screens(
        "PicDetail", Icons.Filled.Settings, Icons.Outlined.Settings
    )

    object Privacy_Policy : Screens(
        "Privacy_Policy", Icons.Filled.Settings, Icons.Outlined.Settings
    )

    object TermsScreen : Screens(
        "TermsScreen", Icons.Filled.Settings, Icons.Outlined.Settings
    )

    fun getTitle(language: String): String {
        return when (this) {
            is ImagesScreen -> when (language) {
                "Hindi" -> "तस्वीरें"
                "Marathi" -> "चित्रे"
                else -> "Images"
            }

            is VideosScreen -> when (language) {
                "Hindi" -> "वीडियो"
                "Marathi" -> "व्हिडिओ"
                else -> "Videos"
            }

            is Saved -> when (language) {
                "Hindi" -> "सहेजा हुआ"
                "Marathi" -> "जतन केलेले"
                else -> "Saved"
            }

            is SettingScreen -> when (language) {
                "Hindi" -> "सेटिंग्स"
                "Marathi" -> "सेटिंग्ज"
                else -> "Settings"
            }

            is VideoDetail -> when (language) {
                "Hindi" -> "वीडियो विवरण"
                "Marathi" -> "व्हिडिओ तपशील"
                else -> "Video Detail"
            }

            is PicDetail -> when (language) {
                "Hindi" -> "चित्र विवरण"
                "Marathi" -> "चित्र तपशील"
                else -> "Picture Detail"
            }

            Privacy_Policy -> when (language) {
                "Hindi" -> "गोपनीयता नीति"
                "Marathi" -> "गोपनीयता धोरण"
                else -> "Privacy Policy"
            }

            TermsScreen -> TODO()
        }
    }
}


@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavEntry() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)

    var selectedLanguage by remember {
        mutableStateOf(sharedPreferences.getString("selectedLanguage", "English") ?: "English")
    }

    LaunchedEffect(selectedLanguage) {
        sharedPreferences.edit().putString("selectedLanguage", selectedLanguage).apply()
    }

    var showBottomNav by remember { mutableStateOf(true) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    showBottomNav = when {
        currentRoute == null -> true
        currentRoute.startsWith(Screens.VideoDetail.route) -> false
        currentRoute.startsWith(Screens.PicDetail.route) -> false
        currentRoute.startsWith(Screens.Privacy_Policy.route) -> false
        currentRoute.startsWith(Screens.TermsScreen.route) -> false
        else -> true
    }

    Scaffold(
        bottomBar = {
            if (showBottomNav) {
                BottomNavigation(
                    navController = navController,
                    selectedLanguage,
                    onLanguageChanged = { newLanguage ->
                        selectedLanguage = newLanguage
                    })
            }
        }
    ) {
        Navigation(
            navController = navController,
            selectedLanguage,
            onLanguageChanged = { newLanguage ->
                selectedLanguage = newLanguage
            })
    }
}


@Composable
fun BottomNavigation(
    navController: NavController,
    selectedLanguage: String,
    onLanguageChanged: (String) -> Unit
) {
    val item = listOf(
        Screens.ImagesScreen,
        Screens.VideosScreen,
        Screens.Saved,
        Screens.SettingScreen
    )

    NavigationBar(containerColor = Color(0xFF673AB7)) {
        val navStck by navController.currentBackStackEntryAsState()
        val current = navStck?.destination?.route
        item.forEach {
            NavigationBarItem(
                selected = current == it.route,
                onClick = {
                    navController.navigate(it.route) {
                        navController.graph.let {
                            it.route?.let { it1 -> popUpTo(it1) }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    if (current == it.route) {
                        Icon(
                            imageVector = it.selectedIcon,
                            contentDescription = "",
                            tint = Color.White
                        )
                    } else {
                        Icon(
                            imageVector = it.unSelectedIcon,
                            contentDescription = "",
                            tint = Color(0xFF01110E)
                        )
                    }
                },
                label = {
                    Text(
                        text = it.getTitle(selectedLanguage),
                        color = if (current == it.route) Color.White else Color(0xFFFFFFFF),
                        fontWeight = FontWeight.Medium
                    )
                },
                colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
            )
        }
    }
}


