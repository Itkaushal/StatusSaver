package com.example.whatsappsaver.Screens


import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import java.io.File

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PicDetail(navController: NavController, PicPath: String, selectedLanguage: String) {
    val context = LocalContext.current
    val permissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        // For Android 13 and above
        rememberPermissionState(permission = android.Manifest.permission.READ_MEDIA_IMAGES)
    } else {
        // For older versions
        rememberPermissionState(permission = android.Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    LaunchedEffect(Unit) {
        permissionState.launchPermissionRequest()
    }

    when {
        permissionState.status.isGranted -> {
            // Ensure PicPath is correctly formatted as a URI
            val imageUri = Uri.fromFile(File(PicPath))

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = "",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )

                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "",
                    tint = Color.Magenta,
                    modifier = Modifier
                        .clickable { navController.navigateUp() }
                        .align(Alignment.TopStart)
                        .padding(6.dp)
                )

                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = "",
                    tint = Color.Magenta,
                    modifier = Modifier
                        .clickable {
                            val file = File(PicPath)
                            downloadImage(context, file)
                        }
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                )
            }
        }
        permissionState.status.shouldShowRationale || !permissionState.status.isGranted -> {
            // Show rationale UI if permission isn't granted
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Storage access is needed to load WhatsApp status images.")
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { permissionState.launchPermissionRequest() }) {
                        Text("Grant Permission")
                    }
                }
            }
        }
        else -> {
            // Handle permission permanently denied case or others
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Permission required to load images. Please enable it in settings.")
            }
        }
    }
}

fun downloadImage(context: Context, file: File) {
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Scoped Storage: Save the file in MediaStore for Android 10 and above
            val contentResolver = context.contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, file.name)
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }

            val imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            if (imageUri != null) {
                contentResolver.openOutputStream(imageUri).use { outputStream ->
                    file.inputStream().use { inputStream ->
                        inputStream.copyTo(outputStream!!)
                    }
                }
                Toast.makeText(context, "Image saved to Downloads", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show()
            }
        } else {
            // For older versions, use the traditional method
            val downloadsFolder = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                file.name
            )
            file.copyTo(downloadsFolder, overwrite = true)

            saveFileToPreferences(context, downloadsFolder.absolutePath)

            Toast.makeText(context, "Image saved to Downloads", Toast.LENGTH_SHORT).show()

            // Refresh media store
            val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            intent.data = Uri.fromFile(downloadsFolder)
            context.sendBroadcast(intent)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Failed to download image", Toast.LENGTH_SHORT).show()
    }
}