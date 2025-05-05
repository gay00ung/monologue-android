package net.ifmain.monologue.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.*
import net.ifmain.monologue.data.api.DiaryApi
import net.ifmain.monologue.data.model.UserEntryDto
import net.ifmain.monologue.data.preference.UserPreferenceManager
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val userPrefs: UserPreferenceManager,
    private val api: DiaryApi,
) : ViewModel() {
    var userId by mutableStateOf("")
    var email by mutableStateOf("")
    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")
    private val _emailFlow = MutableStateFlow("")
    var emailCheckMessage by mutableStateOf<String?>(null)
    var isCheckingEmail by mutableStateOf(false)

    init {
        viewModelScope.launch {
            _emailFlow
                .debounce(500)
                .distinctUntilChanged()
                .collect { emailInput ->
                    isCheckingEmail = true
                    emailCheckMessage = null
                    if (isValidEmail(emailInput)) {
                        val available = isEmailAvailable(emailInput)
                        emailCheckMessage = if (available) {
                            "✅ 사용 가능한 이메일입니다."
                        } else {
                            "❌ 이미 사용 중인 이메일입니다."
                        }
                    }
                    isCheckingEmail = false
                }
        }
    }

    fun signUp(onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            onError("올바른 이메일 형식이 아닙니다."); return
        }
        if (!isValidPassword(password)) {
            onError("비밀번호는 대소문자와 숫자를 포함해야 하며, 8자 이상이어야 합니다."); return
        }
        if (password != confirmPassword) {
            onError("비밀번호가 일치하지 않습니다."); return
        }
        viewModelScope.launch {
            try {
                val resp = api.postSignUp(UserEntryDto(name = username, email = email, password = password))
                if (resp.isSuccessful && resp.body() != null) {
                    val u = resp.body()!!
                    userPrefs.saveSession(u.id, u.name.toString())
                    onSuccess()
                } else {
                    onError("회원가입 실패 (${resp.code()})")
                }
            } catch (e: Exception) {
                onError("네트워크 오류: ${e.localizedMessage}")
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

    suspend fun isEmailAvailable(email: String): Boolean {
        val response = api.checkEmailAvailability(email)
        val body = response.body()
        Log.d("EmailCheck", "Response: ${response.raw()}")
        Log.d("EmailCheck", "Body: $body")
        return try {
            val response = api.checkEmailAvailability(email)
            response.isSuccessful && response.body()?.available == true
        } catch (e: Exception) {
            false
        }
    }

    fun onEmailChanged(newEmail: String) {
        email = newEmail
        _emailFlow.value = newEmail
    }

    fun hasEmptyFields(): Boolean {
        return email.isBlank() || username.isBlank() || password.isBlank() || confirmPassword.isBlank()
    }
}
