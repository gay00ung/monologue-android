package net.ifmain.monologue.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import net.ifmain.monologue.data.api.DiaryApi
import net.ifmain.monologue.data.model.UserDto
import net.ifmain.monologue.data.preference.UserPreferenceManager
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val userPrefs: UserPreferenceManager,
    private val api: DiaryApi,
) : ViewModel() {
    var userId by mutableStateOf("")
    var userName by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")

    fun signIn(
        onSuccess: (userId: String, userName: String) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val resp = api.postSignIn(UserDto(email = email, password = password))
                if (resp.isSuccessful && resp.body() != null) {
                    val u = resp.body()!!
                    userPrefs.saveSession(u.id, u.name ?: "")
                    userId   = u.id
                    userName = u.name ?: ""
                    onSuccess(u.id, u.name ?: "")
                } else {
                    val err = resp.errorBody()?.string()
                    if (!err.isNullOrBlank()) onError(parseErrorMessage(err))
                    else onError("로그인에 실패했습니다. (${resp.code()})")
                }
            } catch (e: Exception) {
                onError("네트워크 오류: ${e.localizedMessage}")
            }
        }
    }

    private fun parseErrorMessage(json: String): String {
        val regex = """"message"\s*:\s*"(.+?)"""".toRegex()
        return regex.find(json)?.groups?.get(1)?.value ?: "알 수 없는 오류가 발생했습니다."
    }

    fun hasEmptyFields(): Boolean {
        return email.isBlank() || password.isBlank()
    }
}