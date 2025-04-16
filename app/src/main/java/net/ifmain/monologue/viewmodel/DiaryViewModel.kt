package net.ifmain.monologue.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import net.ifmain.monologue.data.model.DiaryUiState
import javax.inject.Inject

@HiltViewModel
class DiaryViewModel @Inject constructor() : ViewModel() {
    var uiState by mutableStateOf(DiaryUiState())
        private set

    fun onTextChange(newText: String) {
        uiState = uiState.copy(text = newText)
    }

    fun onMoodSelect(mood: String) {
        uiState = uiState.copy(selectedMood = mood)
    }

    fun onAnalyzeClick() {
        // 감정 분석 API 호출 후 상태 업데이트
    }

    fun onSaveClick() {
        // 로컬 저장 or 서버 API 호출
    }
}
