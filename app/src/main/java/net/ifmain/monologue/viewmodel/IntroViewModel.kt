package net.ifmain.monologue.viewmodel

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
class IntroViewModel @Inject constructor(
    private val userPrefs: UserPreferenceManager,
    private val api: DiaryApi,
) : ViewModel() {
    var isButtonVisible = mutableStateOf(false)
        private set
    var userName by mutableStateOf("")

    fun checkAutoLogin(onAutoLoginSuccess: (String) -> Unit, onLoginFail: () -> Unit) {
        viewModelScope.launch {
            userPrefs.userInfoFlow.collect { (email, password) ->
                if (!email.isNullOrBlank() && !password.isNullOrBlank()) {
                    try {
                        val response = api.postSignIn(UserDto(email, password))
                        if (response.isSuccessful && response.body() != null) {
                            val body = response.body()
                            userName = body?.name.toString()
                            onAutoLoginSuccess(userName)
                        } else {
                            isButtonVisible.value = true
                            onLoginFail()
                        }
                    } catch (e: Exception) {
                        isButtonVisible.value = true
                        onLoginFail()
                    }
                } else {
                    isButtonVisible.value = true
                }
            }
        }
    }
}
