package com.example.whatsappsaver.Screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun Privacy_Policy(navController: NavController) {
    val verticalScroll = rememberScrollState()
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
    var selectedLanguage by remember {
        mutableStateOf(sharedPreferences.getString("selectedLanguage", "English") ?: "English")
    }
    val privacyPolicies = mapOf(
        "English" to listOf(
            "Privacy Policy for WhatsApp Status Saver",
            "Effective Date: October 8, 2024",
            "Welcome to WhatsApp Status Saver (the “Status Saver”). Your privacy is important to us. This Privacy Policy explains how we collect, use, and protect your personal information when you use our App.",
            "1. Information We Collect:",
            "- Personal Information: We do not collect any personal information that can identify you, such as your name, email address, or phone number.",
            "- Usage Data: We may collect information on how you access and use the App, including your device's IP address and usage patterns.",
            "- Media Files: The App allows you to download and save status updates from WhatsApp, but we do not collect or store any media files on our servers.",
            "2. How We Use Your Information:",
            "- To Provide and Maintain Our App: We use your usage data to maintain and improve the functionality of our App.",
            "- To Monitor Usage: We use usage data to enhance user experience and address issues promptly.",
            "- To Communicate with You: We may send you updates related to the App, but we do not collect personal information for this purpose.",
            "3. How We Share Your Information:",
            "- With Your Consent: We may share your information with third parties if you provide explicit consent.",
            "- Legal Requirements: We may disclose your information if required by law or in response to valid requests by public authorities.",
            "4. Data Security:",
            "The security of your data is important to us. We implement reasonable security procedures to protect your information from unauthorized access.",
            "5. Children's Privacy:",
            "Our App does not address anyone under the age of 13. We do not knowingly collect personally identifiable information from children under 13. If you are a parent or guardian and you are aware that your child has provided us with personal data, please contact us.",
            "If you have any questions about this Privacy Policy, please contact us at: kaushalprajapati9953@gmail.com"
        ),

    )

    val selectedPolicy = privacyPolicies[selectedLanguage] ?: privacyPolicies["English"]!!

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(verticalScroll)
            .padding(16.dp)
    ) {
        for (i in selectedPolicy.indices) {
            when (selectedPolicy[i].take(2)) {
                "1." -> ExpandableSection(
                    title = selectedPolicy[i],
                    content = selectedPolicy.subList(i + 1, selectedPolicy.indexOfFirst { it.contains("2.") })
                )
                "2." -> ExpandableSection(
                    title = selectedPolicy[i],
                    content = selectedPolicy.subList(i + 1, selectedPolicy.indexOfFirst { it.contains("3.") })
                )
                "3." -> ExpandableSection(
                    title = selectedPolicy[i],
                    content = selectedPolicy.subList(i + 1, selectedPolicy.indexOfFirst { it.contains("4.") })
                )
                "4." -> ExpandableSection(
                    title = selectedPolicy[i],
                    content = selectedPolicy.subList(i + 1, selectedPolicy.indexOfFirst { it.contains("5.") })
                )
                "5." -> ExpandableSection(
                    title = selectedPolicy[i],
                    content = selectedPolicy.subList(i + 1, selectedPolicy.size)
                )
            }
        }
    }
}

@Composable
fun ExpandableSection(
    title: String,
    content: List<String>,
    initiallyExpanded: Boolean = false,
) {
    var isExpanded by remember { mutableStateOf(initiallyExpanded) }

    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .clickable { isExpanded = !isExpanded }
                .padding(8.dp)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), shape = MaterialTheme.shapes.small)
                .padding(8.dp)
        )

        if (isExpanded) {
            for (item in content) {
                Text(
                    text = item,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 16.dp, bottom = 4.dp)
                )
            }
        }
    }
}