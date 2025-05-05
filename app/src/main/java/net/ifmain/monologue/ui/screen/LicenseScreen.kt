package net.ifmain.monologue.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LicenseScreen() {
    val licenses = listOf(
        LicenseItem("AndroidX Core", "Apache 2.0"),
        LicenseItem("AndroidX Lifecycle", "Apache 2.0"),
        LicenseItem("AndroidX Activity Compose", "Apache 2.0"),
        LicenseItem("AndroidX Compose BOM", "Apache 2.0"),
        LicenseItem("AndroidX UI", "Apache 2.0"),
        LicenseItem("AndroidX Material3", "Apache 2.0"),
        LicenseItem("JUnit", "Eclipse Public License 1.0"),
        LicenseItem("AndroidX Navigation Compose", "Apache 2.0"),
        LicenseItem("Room", "Apache 2.0"),
        LicenseItem("Retrofit", "Apache 2.0"),
        LicenseItem("Gson Converter", "Apache 2.0"),
        LicenseItem("Firebase SDK", "Apache 2.0"),
        LicenseItem("DataStore", "Apache 2.0"),
        LicenseItem("Hilt (Dagger)", "Apache 2.0"),
        LicenseItem("Kotlin Coroutines", "Apache 2.0"),
        LicenseItem("Accompanist", "Apache 2.0"),
        LicenseItem("Coil (Image Loading)", "Apache 2.0"),
        LicenseItem("MSZ Progress Indicator", "MIT"),
        LicenseItem("WorkManager", "Apache 2.0")
    )
    Scaffold(
        topBar = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "License",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                )
            }
        },
        content = { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(top = 16.dp)
                    .padding(horizontal = 16.dp)
            ) {
                items(licenses) { license ->
                    LicenseRow(license)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                }
            }
        }
    )
}

@Composable
fun LicenseRow(license: LicenseItem) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = license.libraryName,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "License: ${license.licenseType}",
            style = MaterialTheme.typography.bodySmall,
            color = DarkGray
        )
    }
}

data class LicenseItem(val libraryName: String, val licenseType: String)
