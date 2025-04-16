package net.ifmain.monologue.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import net.ifmain.monologue.data.model.DiaryEntry

@Composable
fun DiaryListScreen(
    entries: List<DiaryEntry>,
    onEntryClick: (DiaryEntry) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
//        items(entries) { entry ->
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 6.dp)
//                    .clickable { onEntryClick(entry) },
//                elevation = CardDefaults.cardElevation(4.dp)
//            ) {
//                Column(modifier = Modifier.padding(16.dp)) {
//                    Row(
//                        horizontalArrangement = Arrangement.SpaceBetween,
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        Text(
//                            text = entry.date,
//                            style = MaterialTheme.typography.labelLarge
//                        )
//                        Text(
//                            text = entry.mood ?: "❓",
//                            fontSize = 20.sp
//                        )
//                    }
//                    Spacer(Modifier.height(4.dp))
//                    Text(
//                        text = entry.text,
//                        style = MaterialTheme.typography.bodyLarge,
//                        maxLines = 2,
//                        overflow = TextOverflow.Ellipsis
//                    )
//                }
//            }
//        }
    }
}
