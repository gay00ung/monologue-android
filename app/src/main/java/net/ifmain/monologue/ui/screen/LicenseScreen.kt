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
import net.ifmain.monologue.ui.theme.Cream
import net.ifmain.monologue.ui.theme.Honey

@Composable
fun LicenseScreen() {
    val licenses = listOf(
        LicenseItem("AndroidX Core KTX", "Apache 2.0"),
        LicenseItem("AndroidX Lifecycle Runtime KTX", "Apache 2.0"),
        LicenseItem("AndroidX Activity Compose", "Apache 2.0"),
        LicenseItem("Jetpack Compose BOM", "Apache 2.0"),
        LicenseItem("Jetpack Compose UI", "Apache 2.0"),
        LicenseItem("Jetpack Compose UI Graphics", "Apache 2.0"),
        LicenseItem("Jetpack Compose UI Tooling Preview", "Apache 2.0"),
        LicenseItem("Material3 (Compose)", "Apache 2.0"),
        LicenseItem("AndroidX Navigation Compose", "Apache 2.0"),
        LicenseItem("Room (Jetpack)", "Apache 2.0"),
        LicenseItem("Retrofit", "Apache 2.0"),
        LicenseItem("Gson Converter", "Apache 2.0"),
        LicenseItem("Firebase Analytics", "Apache 2.0"),
        LicenseItem("Jetpack DataStore Preferences", "Apache 2.0"),
        LicenseItem("Hilt (Dagger)", "Apache 2.0"),
        LicenseItem("AndroidX Lifecycle ViewModel Compose", "Apache 2.0"),
        LicenseItem("AndroidX Hilt Navigation Compose", "Apache 2.0"),
        LicenseItem("OkHttp", "Apache 2.0"),
        LicenseItem("PersistentCookieJar", "Apache 2.0"),
        LicenseItem("Moshi Converter", "Apache 2.0"),
        LicenseItem("JUnit", "Eclipse Public License 1.0"),
        LicenseItem("Espresso Core", "Apache 2.0"),
        LicenseItem("AndroidX UI Test JUnit4", "Apache 2.0"),
        LicenseItem("AndroidX UI Tooling", "Apache 2.0"),
        LicenseItem("AndroidX UI Test Manifest", "Apache 2.0")
    )
    Scaffold(
        containerColor = Cream,
        topBar = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "License",
                    style = MaterialTheme.typography.headlineLarge,
                    textAlign = TextAlign.Center,
                    color = Honey,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
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
