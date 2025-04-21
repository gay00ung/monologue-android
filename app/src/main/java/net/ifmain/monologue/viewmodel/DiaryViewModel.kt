package net.ifmain.monologue.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import net.ifmain.monologue.data.model.DiaryEntry
import net.ifmain.monologue.data.model.DiaryUiState
import net.ifmain.monologue.data.repository.DiaryRepository
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class DiaryViewModel @Inject constructor(
    private val repository: DiaryRepository
) : ViewModel() {
    var uiState by mutableStateOf(DiaryUiState())
        private set
    var userId by mutableStateOf<String>("")

    fun onTextChange(newText: String) {
        uiState = uiState.copy(text = newText)
    }

    fun onMoodSelect(mood: String) {
        uiState = uiState.copy(selectedMood = mood)
    }

    fun onSaveClick(onError: (String) -> Unit, onSuccess: () -> Unit) {
        if (uiState.selectedMood.isBlank()) {
            onError("감정을 선택해 주세요")
            return
        }

        val finalText = if (uiState.text.isBlank()) "기록없음" else uiState.text

        println("💾 저장됨: 텍스트=$finalText, 감정=${uiState.selectedMood}")

        val currentUserId = userId
        if (false) {
            onError("유저 정보가 없습니다.")
            return
        }

        onSuccess()
        saveDiary(uiState, currentUserId)
    }

    fun saveDiary(uiState: DiaryUiState, userId: String) {
        val today = LocalDate.now().toString()
        val entry = DiaryEntry(
            date = today,
            text = if (uiState.text.isBlank()) "기록 없음" else uiState.text,
            mood = uiState.selectedMood,
            isSynced = false
        )

        viewModelScope.launch {
            repository.saveEntry(entry, userId)
        }
    }

    fun onAnalyzeClick() {
        // 감정 분석 API 호출 후 상태 업데이트
    }

}

