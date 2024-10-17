package com.example.whatsappsaver.Screens


import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.whatsappsaver.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun WhatsAppStatusScreen(navController: NavController, selectedLanguage: String) {
    val permissionState = rememberPermissionState(permission = android.Manifest.permission.READ_EXTERNAL_STORAGE)

    LaunchedEffect(Unit) {
        permissionState.launchPermissionRequest()
    }

    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
    var selectedLanguage by remember {
        mutableStateOf(sharedPreferences.getString("selectedLanguage", "English") ?: "English")
    }

    var showPurchase by remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (selectedLanguage == "Hindi" || selectedLanguage == "Marathi") {
                            Image(
                                painter = painterResource(id = R.drawable.premium),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(26.dp)
                                    .clickable {
                                        showPurchase = true
                                    }
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = when (selectedLanguage) {
                                "Hindi" -> "स्थिति छवि"
                                "Marathi" -> "स्थिती प्रतिमा"
                                else -> "Status Images"
                            },
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                },
                actions = {
                    if (selectedLanguage != "Hindi" && selectedLanguage != "Marathi") {
                        IconButton(onClick = { showPurchase = true }) {
                            Icon(
                                painter = painterResource(id = R.drawable.premium),
                                contentDescription = "Premium",
                                modifier = Modifier.size(26.dp),
                                tint = Color.White
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF673AB7))
            )
        },
        content = { paddingValues ->

            if (permissionState.status.isGranted) {

                DisplayWhatsAppStatuses(navController)

            } else {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = paddingValues.calculateTopPadding()),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Permission Denied",
                            tint = Color.Gray,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Storage access is required to view WhatsApp statuses.",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { permissionState.launchPermissionRequest() }) {
                            Text("Grant Permission")
                        }
                    }
                }
            }

            if (showPurchase) {
                showPremiumFeatureDialog(LocalContext.current) {
                    showPurchase = false
                }
            }
        }
    )
}

@Composable
fun showPremiumFeatureDialog(context: Context, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Premium Features") },
        text = {
            Column {
                Text("Unlock the premium version for these features:")
                Spacer(modifier = Modifier.height(10.dp))
                Text("• Remove Ads")
                Text("• Access exclusive content")
                Text("• Get priority support")
            }
        },
        confirmButton = {
            TextButton(onClick = {
                Toast.makeText(
                    context,
                    "coming soon!",
                    Toast.LENGTH_SHORT
                ).show()
                onDismiss()
            }) {
                Text("Get Premium")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Cancel")
            }
        }
    )
}