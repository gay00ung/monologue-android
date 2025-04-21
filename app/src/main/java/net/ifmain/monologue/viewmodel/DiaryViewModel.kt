package net.ifmain.monologue.viewmodel

import android.util.Log
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
    var isSaving by mutableStateOf(false)

    fun onTextChange(newText: String) {
        uiState = uiState.copy(text = newText)
        Log.d("DiaryViewModel", "Text changed: $newText")
    }

    fun onMoodSelect(mood: String) {
        uiState = uiState.copy(selectedMood = mood)
        Log.d("DiaryViewModel", "Mood selected: $mood")
    }

    fun onSaveClick(onError: (String) -> Unit, onSuccess: () -> Unit) {
        if (isSaving) return
        isSaving = true

        if (uiState.selectedMood.isBlank()) {
            onError("감정을 선택해 주세요")
            isSaving = false
            return
        }

        val finalText = if (uiState.text.isBlank()) "기록없음" else uiState.text
        println("💾 저장됨: 텍스트=$finalText, 감정=${uiState.selectedMood}")

        val currentUserId = userId
        Log.d("DiaryViewModel", "onSaveClick triggered with mood=${uiState.selectedMood} and text=$finalText")

        if (currentUserId.isBlank()) {
            onError("유저 정보가 없습니다.")
            isSaving = false
            return
        }

        viewModelScope.launch {
            try {
                saveDiary(uiState, currentUserId)
                onSuccess()
            } catch (e: Exception) {
                onError("저장 중 오류가 발생했습니다.")
                Log.e("DiaryViewModel", "Error saving diary", e)
            } finally {
                isSaving = false
            }
        }
    }

    suspend fun saveDiary(uiState: DiaryUiState, userId: String) {
        val today = LocalDate.now().toString()
        val entry = DiaryEntry(
            date = today,
            text = if (uiState.text.isBlank()) "기록 없음" else uiState.text,
            mood = uiState.selectedMood,
            isSynced = false
        )

        try {
            repository.saveEntry(entry, userId)
            Log.d("DiaryViewModel", "Diary saved successfully!")
        } catch (e: Exception) {
            Log.e("DiaryViewModel", "Error saving diary", e)
        }
    }

    fun onAnalyzeClick() {
        // 감정 분석 API 호출 후 상태 업데이트
    }

}

