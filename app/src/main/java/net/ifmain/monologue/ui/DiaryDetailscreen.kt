package net.ifmain.monologue.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import net.ifmain.monologue.data.model.DiaryEntry
import net.ifmain.monologue.ui.component.TitleBar
import net.ifmain.monologue.ui.theme.Cream
import net.ifmain.monologue.viewmodel.DiaryViewModel

@Composable
fun DiaryDetailScreen(
) {

    Scaffold(
        containerColor = Cream,
        topBar = {
            TitleBar()
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

        }
    }
}