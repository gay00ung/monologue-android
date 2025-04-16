package net.ifmain.monologue.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.ifmain.monologue.data.model.DiaryUiState
import net.ifmain.monologue.ui.component.TitleBar
import net.ifmain.monologue.ui.theme.Cream
import net.ifmain.monologue.ui.theme.Honey
import net.ifmain.monologue.ui.theme.Lemon
import androidx.compose.foundation.lazy.items

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    uiState: DiaryUiState,
    onTextChange: (String) -> Unit,
    onMoodSelect: (String) -> Unit,
    onAnalyzeClick: () -> Unit,
    onSaveClick: () -> Unit,
    onNavigateToDiaryList: () -> Unit,
) {
    val moods = listOf("😊", "😐", "😢", "😡", "😴", "😍", "❓")

    Scaffold (
        containerColor = Cream,
        topBar = {
            TitleBar()
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "오늘의 한 줄",
                style = MaterialTheme.typography.headlineSmall,
                color = Honey,
            )

            OutlinedTextField(
                value = uiState.text,
                onValueChange = onTextChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("오늘 하루를 한 줄로 남겨보세요") },
                maxLines = 3,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Lemon,
                    unfocusedBorderColor = Lemon,
                    cursorColor = Lemon,
                    focusedLabelColor = Lemon
                ),
                textStyle = TextStyle(color = Color.Black)
            )

            Text("감정을 선택해보세요")
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(items = moods, key = { it }) { mood ->
                    val isSelected = uiState.selectedMood == mood
                    Text(
                        text = mood,
                        fontSize = 28.sp,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(
                                if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                else Color.Transparent
                            )
                            .padding(8.dp)
                            .clickable { onMoodSelect(mood) }
                    )
                }
            }

            // 추후 구현 예정
//            Button(
//                onClick = onAnalyzeClick,
//                modifier = Modifier.fillMaxWidth(),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Lemon,
//                    contentColor = Cream
//                )
//            ) {
//                Text("✨ 감정 분석하기")
//            }

            Button(
                onClick = onSaveClick,
                enabled = uiState.text.isNotBlank(),
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Lemon,
                    contentColor = Cream
                )
            ) {
                Text("📂저장하기")
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    val uiState = DiaryUiState(
        text = "오늘은 정말 좋은 날이었어요!",
        selectedMood = "😊"
    )

    HomeScreen(
        uiState = uiState,
        onTextChange = {},
        onMoodSelect = {},
        onAnalyzeClick = {},
        onSaveClick = {},
        onNavigateToDiaryList = { /* TODO */ }
    )
}
