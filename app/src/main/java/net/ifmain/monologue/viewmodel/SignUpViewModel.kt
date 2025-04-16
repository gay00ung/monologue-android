package net.ifmain.monologue.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.ifmain.monologue.data.UserEntryDto
import net.ifmain.monologue.data.api

class SignUpViewModel : ViewModel() {
    var email by mutableStateOf("")
    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")

    fun signUp(onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (!isValidEmail(email)) {
            onError("올바른 이메일 형식이 아닙니다.")
            return
        }

        if (!isValidPassword(password)) {
            onError("비밀번호는 대소문자와 숫자를 포함해야 하며, 8자 이상이어야 합니다.")
            return
        }

        if (password != confirmPassword) {
            onError("비밀번호가 일치하지 않습니다.")
            return
        }

        viewModelScope.launch {
            try {
                val response = api.postUser(
                    UserEntryDto(
                        name = username,
                        email = email,
                        password = password
                    )
                )

                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError("회원가입에 실패했습니다. (${response.code()})")
                }
            } catch (e: Exception) {
//                onError("에러 발생: ${e.localizedMessage}")
                Log.d("SignUpViewModel", "Error: ${e.localizedMessage}")
            }
        }
    }

    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPassword(password: String): Boolean {
        val passwordRegex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$")
        return passwordRegex.matches(password)
    }

    fun hasEmptyFields(): Boolean {
        return email.isBlank() || username.isBlank() || password.isBlank() || confirmPassword.isBlank()
    }

}
