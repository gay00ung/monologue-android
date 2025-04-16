package net.ifmain.monologue.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.ifmain.monologue.data.UserDto
import net.ifmain.monologue.data.api

class SignInViewModel : ViewModel() {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var userName by mutableStateOf("")

    fun signIn(onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = api.postSignIn(
                    UserDto(
                        email = email,
                        password = password
                    )
                )

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        userName = body.name.toString()
                        onSuccess(userName)
                    } else {
                        onError("응답이 비어있습니다.")
                    }
                } else {
                    onError("로그인에 실패했습니다. (${response.code()})")
                }
            } catch (e: Exception) {
//                onError("에러 발생: ${e.localizedMessage}")
                Log.d("SignInViewModel", "Error: ${e.localizedMessage}")
            }
        }
    }

    fun hasEmptyFields(): Boolean {
        return email.isBlank() || password.isBlank()
    }
}
