@file:Suppress("NAME_SHADOWING")

package com.example.whatsappsaver.Screens


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.AdminPanelSettings
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.PlaylistAddCheckCircle
import androidx.compose.material.icons.outlined.PrivacyTip
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.StarRate
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material.icons.outlined.Whatsapp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.whatsappsaver.navigation.Screens

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SettingScreen(
    navController: NavController, selectedLanguage: String, onLanguageChanged: (String) -> Unit
) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
    var selectedLanguage by remember {
        mutableStateOf(sharedPreferences.getString("selectedLanguage", "English") ?: "English")
    }

    var rate by remember {
        mutableStateOf(false)
    }
    //val pakagename = context.applicationInfo.packageName
    var showLanguageDialog by remember { mutableStateOf(false) }

    val layoutDirection = when (selectedLanguage) {
        "Hindi" -> LayoutDirection.Rtl
        else -> LayoutDirection.Ltr
    }
    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        putExtra(Intent.EXTRA_TEXT, "https://github.com/Itkaushal/StatusSaver/")
        type = "text/plain"
    }
    val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
    val appVersion = packageInfo.versionName
    val shareIntent = Intent.createChooser(sendIntent, null)
    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
        Scaffold(topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (selectedLanguage) {
                            "Hindi" -> "सेटिंग्स"
                            else -> "Settings"
                        }, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF17A752)),
                expandedHeight = 40.dp
            )
        }) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 100.dp, start = 16.dp, bottom = 16.dp, end = 16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                RowWithIcon(
                    icon = Icons.Outlined.Place, title = when (selectedLanguage) {
                        "Hindi" -> "भाषाएँ"
                        else -> "Languages"
                    }, subtitle = when (selectedLanguage) {
                        "Hindi" -> "चयनित भाषा: ${selectedLanguage}"
                        else -> "Selected Language: ${selectedLanguage}"
                    }, onClick = { showLanguageDialog = true }, selectedLanguage = selectedLanguage
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = when (selectedLanguage) {
                        "Hindi" -> "अन्य"
                        else -> "Others"
                    },
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Start)
                )


                RowWithIcon(
                    icon = Icons.Outlined.Whatsapp, title = when (selectedLanguage) {
                        "Hindi" -> "ऐप शेयर करें"
                        else -> "Share App"
                    }, subtitle = when (selectedLanguage) {
                        "Hindi" -> "ऐप अपने दोस्तों के साथ शेयर करें"
                        else -> "Share app with friends and family"
                    }, onClick = {
                        context.startActivity(shareIntent)
                    }, selectedLanguage = selectedLanguage
                )


                RowWithIcon(
                    icon = Icons.Outlined.StarRate, title = when (selectedLanguage) {
                        "Hindi" -> "हमें रेट करें"
                        else -> "Rate Us"
                    }, subtitle = when (selectedLanguage) {
                        "Hindi" -> "हमें रेट करें"
                        else -> "Please Rate Our App."
                    }, onClick = {
                        rate = true
                    }, selectedLanguage = selectedLanguage
                )


                RowWithIcon(
                    icon = Icons.Outlined.PrivacyTip, title = when (selectedLanguage) {
                        "Hindi" -> "गोपनीयता नीति"
                        else -> "Privacy Policy"
                    }, subtitle = when (selectedLanguage) {
                        "Hindi" -> "हमारी गोपनीयता नीति पढ़ेंں"
                        else -> "Read our privacy policy carefully"
                    }, selectedLanguage = selectedLanguage, onClick = {
                        navController.navigate(Screens.Privacy_Policy.route)
                    }
                )

                RowWithIcon(
                    icon = Icons.Outlined.PlaylistAddCheckCircle, title = when (selectedLanguage) {
                        "Hindi" -> "नियम और शर्तें"
                        else -> "Terms & Conditions"
                    }, subtitle = when (selectedLanguage) {
                        "Hindi" -> "हमारी नियम और शर्तें पढ़ें"
                        else -> "Read our terms & conditions carefully"
                    }, selectedLanguage = selectedLanguage, onClick = {
                        navController.navigate(Screens.TermsScreen.route)
                    }
                )


                RowWithIcon(
                    icon = Icons.Outlined.AdminPanelSettings, title = when (selectedLanguage) {
                        "Hindi" -> "संस्करण: $appVersion"
                        else -> "Version: $appVersion"
                    }, selectedLanguage = selectedLanguage, subtitle = null
                )
            }

            if (rate) {
                val starSize = 40.dp
                val starColor = Color(0XFFFFC107)
                var selectedRating by remember { mutableStateOf(0) }
                AlertDialog(
                    onDismissRequest = { rate = false },
                    title = {
                        Text(
                            text = when (selectedLanguage) {
                                "Hindi" -> "हमें रेट करें"
                                else -> "Rate Us"
                            },
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    },
                    text = {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = when (selectedLanguage) {
                                    "Hindi" -> "कृपया हमारी ऐप को रेट करें"
                                    else -> "Please rate our app"
                                },
                                fontSize = 16.sp,
                                color = Color.Gray
                            )

                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                for (i in 1..5) {
                                    val isSelected = i <= selectedRating
                                    Icon(
                                        imageVector = if (isSelected) Icons.Filled.Star else Icons.Filled.Star,
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clickable { selectedRating = i }
                                            .scale(if (isSelected) 1.2f else 1f)
                                            .animateContentSize(),
                                        contentDescription = null,
                                        tint = if (isSelected) starColor else Color.LightGray
                                    )
                                }
                            }
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                rate = false
                                Toast.makeText(context, "Rate Submit", Toast.LENGTH_SHORT).show()
                            },
                            enabled = selectedRating > 0,
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
                        ) {
                            Text(
                                text = when (selectedLanguage) {
                                    "Hindi" -> "प्रस्तुत करें"
                                    else -> "Submit"
                                },
                                color = Color.White
                            )
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { rate = false }) {
                            Text(
                                text = when (selectedLanguage) {
                                    else -> "Cancel"
                                }
                            )
                        }
                    },
                    shape = RoundedCornerShape(16.dp)
                )
            }

            if (showLanguageDialog) {
                LanguageSelectionDialog(selectedLanguage = selectedLanguage,
                    onLanguageSelected = { selected ->
                        selectedLanguage = selected
                        sharedPreferences.edit().putString("selectedLanguage", selectedLanguage)
                            .apply()
                        onLanguageChanged(selected)
                    },
                    onDismissRequest = { showLanguageDialog = false })
            }
        }
    }
}

@Composable
fun RowWithIcon(
    icon: ImageVector,
    title: String,
    subtitle: String?,
    onClick: (() -> Unit)? = null,
    selectedLanguage: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onClick?.invoke() })
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .border(
                        BorderStroke(1.dp, Color.LightGray), shape = RoundedCornerShape(8.dp)
                    ), contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.DarkGray
                )

                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        color = Color.LightGray,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Icon(
            imageVector = Icons.Outlined.ArrowForward,
            contentDescription = null,
            tint = Color.LightGray,
            modifier = Modifier
                .size(20.dp)
                .rotate(
                    when (selectedLanguage) {
                        "Hindi" -> 180f
                        else -> 0f
                    }

                )
        )
    }
}


@Composable
fun LanguageSelectionDialog(
    selectedLanguage: String,
    onLanguageSelected: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    var tempSelectedLanguage by remember { mutableStateOf(selectedLanguage) }

    val languages = listOf("English", "Hindi")

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = "Select Language", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        },
        text = {
            Column {
                languages.forEach { language ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (language == tempSelectedLanguage),
                                onClick = { tempSelectedLanguage = language }
                            )
                            .padding(8.dp)
                    ) {
                        RadioButton(
                            selected = (language == tempSelectedLanguage),
                            onClick = { tempSelectedLanguage = language }
                        )
                        Text(
                            text = language,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onLanguageSelected(tempSelectedLanguage)
                onDismissRequest()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    )
}

