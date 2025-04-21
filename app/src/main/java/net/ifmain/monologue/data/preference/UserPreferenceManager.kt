package net.ifmain.monologue.data.preference

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferenceManager @Inject constructor(private val context: Context) {
    private val dataStore = context.dataStore

    val userInfoFlow: Flow<Pair<String?, String?>> = dataStore.data
        .map { prefs ->
            val userId = prefs[UserPreferenceKeys.USER_ID]
            val name = prefs[UserPreferenceKeys.USER_NAME]
            val email = prefs[UserPreferenceKeys.USER_EMAIL]
            val password = prefs[UserPreferenceKeys.USER_PASSWORD]
            Pair(email, password)
        }

    suspend fun saveUserInfo(
        userId: String,
        name: String,
        email: String,
        password: String
    ) {
        dataStore.edit { prefs ->
            prefs[UserPreferenceKeys.USER_ID] = userId
            prefs[UserPreferenceKeys.USER_NAME] = name
            prefs[UserPreferenceKeys.USER_EMAIL] = email
            prefs[UserPreferenceKeys.USER_PASSWORD] = password
        }
    }

    suspend fun clearUserInfo() {
        dataStore.edit { it.clear() }
    }
}

