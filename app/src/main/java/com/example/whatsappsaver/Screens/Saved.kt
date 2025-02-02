package com.example.whatsappsaver.Screens

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.whatsappsaver.navigation.Screens
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Saved(navController: NavController, selectedLanguage: String) {
    val context = LocalContext.current
    val savedFiles = remember { mutableStateListOf<String>() }
    LaunchedEffect(Unit) {
        val files = getSavedFilesFromPreferences(context)

        if (files.isEmpty()) {
            Log.e("SavedMedia", "No saved files found!")
            Toast.makeText(context, "No saved files found", Toast.LENGTH_SHORT).show()
        } else {
            savedFiles.clear()

            // ✅ Ensure file exists before adding to the list
            val validFiles = files.filter { File(it).exists() }

            if (validFiles.isEmpty()) {
                Log.e("SavedMedia", "Stored paths do not exist on device!")
                Toast.makeText(context, "Saved files not found on device", Toast.LENGTH_SHORT).show()
            }

            savedFiles.addAll(validFiles)

            // ✅ Log the saved files to check
            Log.d("SavedMedia", "Saved files loaded: $savedFiles")
        }
    }



    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    text = when (selectedLanguage) {
                        "Hindi" -> "स्थिति सहेजी गई"
                        "English" -> "Status Saved"
                        else -> "Status Saved"
                    },
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF17A752)),
                expandedHeight = 40.dp)
        },
    ) {
        if (savedFiles.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Text("No saved files")
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = it.calculateTopPadding()),
                contentPadding = PaddingValues(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(savedFiles) { filePath ->
                    val file = File(filePath)
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .aspectRatio(1f)
                            .clickable {
                                if (file.extension == "mp4") {
                                    navController.navigate(
                                        Screens.VideoDetail.route + "/${
                                            Uri.encode(
                                                filePath
                                            )
                                        }"
                                    )
                                } else {
                                    navController.navigate(
                                        Screens.PicDetail.route + "/${
                                            Uri.encode(
                                                filePath
                                            )
                                        }"
                                    )
                                }
                            }
                    ) {
                        if (file.extension in listOf("jpg", "png","jpeg")) {
                            AsyncImage(
                                model = filePath,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else if (file.extension == "mp4") {
                            val thumbnail = remember(filePath) {
                                loadVideoThumbnail(file)
                            }
                            if (thumbnail != null) {
                                Image(
                                    bitmap = thumbnail.asImageBitmap(),
                                    contentDescription = "Video Thumbnail",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Play Icon",
                                tint = Color.White,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }
            }
        }
    }
}
