package net.ifmain.monologue.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import net.ifmain.monologue.data.api.DiaryApi
import net.ifmain.monologue.data.preference.UserPreferenceManager
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userPrefs: UserPreferenceManager,
    private val api: DiaryApi,
) : ViewModel() {
    fun logout(onComplete: () -> Unit) {
        viewModelScope.launch {
            api.postLogout()
            userPrefs.clearCookie()
            userPrefs.clearSession()
            onComplete()
        }
    }

    fun withdraw(onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val resp = api.deleteUser()
                if (resp.isSuccessful) {
                    userPrefs.clearCookie()
                    userPrefs.clearSession()
                    onSuccess()
                } else {
                    onError(resp.errorBody()?.string() ?: "회원탈퇴 실패 (${resp.code()})")
                }
            } catch (e: Exception) {
                onError("네트워크 오류: ${e.localizedMessage}")
            }
        }
    }
}