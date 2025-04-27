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

    fun signIn(onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = api.postSignIn(
                    UserDto(
                        id = userId,
                        name = userName,
                        email = email,
                        password = password
                    )
                )

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        userName = body.name.toString()
                        userPrefs.saveUserInfo(userId, userName, email, password)
                        onSuccess(userName)
                    } else {
                        onError("응답이 비어있습니다.")
                    }
                } else {
                    val errorMsg = response.errorBody()?.string()
                    if (!errorMsg.isNullOrBlank()) {
                        onError(parseErrorMessage(errorMsg))
                    } else {
                        onError("로그인에 실패했습니다. (${response.code()})")
                    }
                }
            } catch (e: Exception) {
                Log.d("SignInViewModel", "Error: ${e.localizedMessage}")
                onError("네트워크 오류가 발생했습니다.")
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