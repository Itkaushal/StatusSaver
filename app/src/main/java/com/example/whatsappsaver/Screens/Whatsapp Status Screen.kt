@file:OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)

package com.example.whatsappsaver.Screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.google.accompanist.permissions.*

@Composable
fun WhatsAppStatusScreen(navController: NavController, selectedLanguage: String) {
    val context = LocalContext.current

    // ✅ Correctly handling multiple permissions
    val permissionState = rememberMultiplePermissionsState(
        permissions = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            listOf(android.Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            listOf(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }
    )

    // ✅ Ensure permission request happens dynamically
    LaunchedEffect(permissionState.permissions) {
        if (!permissionState.allPermissionsGranted) {
            permissionState.launchMultiplePermissionRequest()
        }
    }

    var showPurchase by remember { mutableStateOf(false) }

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
                                    .clickable { showPurchase = true }
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF17A752)),
                expandedHeight = 40.dp
            )
        },
        content = { paddingValues ->
            if (permissionState.allPermissionsGranted) {
                // ✅ Correctly showing UI when permission is granted
                DisplayWhatsAppStatuses(navController)
            } else {
                // ❌ Permission Not Granted UI
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
                        Button(onClick = {
                            permissionState.launchMultiplePermissionRequest()
                        }) {
                            Text("Grant Permission")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = {
                            openAppSettings(context)
                        }) {
                            Text("Open Settings")
                        }
                    }
                }
            }

            if (showPurchase) {
                showPremiumFeatureDialog(context) { showPurchase = false }
            }
        }
    )
}

// ✅ Opens app settings if permission is permanently denied
fun openAppSettings(context: Context) {
    val intent = Intent(
        android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", context.packageName, null)
    )
    context.startActivity(intent)
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
                Toast.makeText(context, "Coming soon!", Toast.LENGTH_SHORT).show()
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
