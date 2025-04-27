package net.ifmain.monologue.ui.screen

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.ifmain.monologue.data.model.DiaryUiState
import net.ifmain.monologue.ui.component.TitleBar
import net.ifmain.monologue.ui.theme.Cream
import net.ifmain.monologue.ui.theme.Honey
import net.ifmain.monologue.ui.theme.Lemon
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.ifmain.monologue.viewmodel.DiaryViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DiaryHomeScreen(
    viewModel: DiaryViewModel,
    onTextChange: (String) -> Unit,
    onMoodSelect: (String) -> Unit,
    onAnalyzeClick: () -> Unit,
    onSaveClick: (String?, String) -> Unit,
    onNavigateToDiaryList: () -> Unit,
) {
    val uiState = viewModel.uiState
    val context = LocalContext.current
    val moods = listOf("😊", "😐", "😢", "😡", "😴", "😍", "❓")
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
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
                onValueChange = {
                    onTextChange(it)
                    Log.d("DiaryHomeScreen", "Text changed: $it")
                },

                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "오늘 하루를 한 줄로 남겨보세요",
                        style = TextStyle(color = LightGray)
                    )
                },
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
                            .clickable {
                                Log.d("DiaryHomeScreen", "Mood clicked: $mood")
                                onMoodSelect(mood)
                            }
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
                onClick = {
                    if (uiState.selectedMood.isBlank()) {
                        Toast.makeText(context, "감정을 선택해 주세요", Toast.LENGTH_SHORT).show()
                    } else {
                        viewModel.onSaveClick(
                            onError = { errorMessage ->
                                if (errorMessage.contains("이미 등록하셨습니다")) {
                                    showDialog = true
                                } else {
                                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                                }
                            },
                            onSuccess = {
                                Toast.makeText(context, "저장되었습니다!", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                },
                enabled = true,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Lemon,
                    contentColor = Cream
                )
            ) {
                Text("📂저장하기")
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    shape = RoundedCornerShape(16.dp),
                    containerColor = Cream,
                    icon = {
                        Text(
                            text = "📝",
                            fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                    title = {
                        Text(
                            text = "오늘은 이미 등록하셨습니다.",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Lemon,
                                textAlign = TextAlign.Center
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    text = {
                        Text(
                            text = "저장된 내용을 수정하시겠어요?",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = Color.DarkGray,
                                textAlign = TextAlign.Center
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                viewModel.updateDiary(uiState, viewModel.userId)
                                Toast.makeText(context, "수정되었습니다!", Toast.LENGTH_SHORT).show()
                                showDialog = false
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.padding(8.dp),
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = Color.White,
                                containerColor = Lemon
                            )
                        ) {
                            Text(
                                text = "확인",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showDialog = false },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.padding(8.dp),
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = Color(0xFF9E9E9E), // 회색 버튼 색상
                                containerColor = Color.Transparent
                            )
                        ) {
                            Text(
                                text = "취소",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                )
            }
        }
    }
}
