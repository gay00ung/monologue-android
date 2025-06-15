package net.ifmain.monologue.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import net.ifmain.monologue.data.model.DiaryEntry
import net.ifmain.monologue.ui.component.TitleBar
import net.ifmain.monologue.ui.theme.Cream
import net.ifmain.monologue.ui.theme.Lemon
import net.ifmain.monologue.viewmodel.DiaryViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import net.ifmain.monologue.R

@Composable
fun DiaryListScreen(
    viewModel: DiaryViewModel,
    userId: String,
    onNavigateToDiaryDetail: (DiaryEntry) -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val entries by viewModel.diaryEntries.collectAsStateWithLifecycle()

    LaunchedEffect(userId) {
        viewModel.initialize(userId)
    }

    Scaffold(
        containerColor = Cream,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(modifier = Modifier.width(48.dp))
                Box(
                    modifier = Modifier
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    TitleBar()
                }
                Box(
                    modifier = Modifier
                        .padding(top = 32.dp, end = 16.dp)
                        .pointerInput(Unit) {
                            detectTapGestures {
                                onNavigateToSettings()
                            }
                        }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.setting),
                        contentDescription = null,
                        modifier = Modifier
                            .size(48.dp)
                            .align(Alignment.Center),
                        colorFilter = ColorFilter.tint(LightGray)
                    )
                }
            }


        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(entries) { entry ->
                DiaryCard(entry = entry, onEntryClick = onNavigateToDiaryDetail)
            }
        }
    }
}

@Composable
fun DiaryCard(
    entry: DiaryEntry,
    onEntryClick: (DiaryEntry) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEntryClick(entry) },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = formatDate(entry.date),
                fontSize = 14.sp,
                color = Lemon,
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = entry.text,
                    fontSize = 16.sp,
                    color = Color.DarkGray,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = entry.mood ?: "❓",
                    fontSize = 24.sp,
                    modifier = Modifier.padding(start = 8.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

private fun formatDate(date: String): String {
    return try {
        val parsedDate = LocalDate.parse(date)
        parsedDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
    } catch (e: Exception) {
        date
    }
}
