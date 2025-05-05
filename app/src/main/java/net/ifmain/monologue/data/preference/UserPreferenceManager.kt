package net.ifmain.monologue.data.preference

import android.content.Context
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferenceManager @Inject constructor(private val context: Context) {
    private val ds = context.dataStore

    private val USER_COOKIE = stringPreferencesKey("user_cookie")

    val cookieFlow: Flow<String?> = ds.data
        .map { prefs -> prefs[USER_COOKIE] }

    suspend fun saveCookie(cookie: String) {
        ds.edit { prefs ->
            prefs[USER_COOKIE] = cookie
        }
    }

    suspend fun clearCookie() {
        ds.edit { prefs ->
            prefs.remove(USER_COOKIE)
        }
    }

    val sessionFlow: Flow<Pair<String?, String?>> = ds.data
        .map { prefs ->
            prefs[UserPreferenceKeys.USER_ID] to prefs[UserPreferenceKeys.USER_NAME]
        }

    suspend fun saveSession(userId: String, userName: String) {
        ds.edit { prefs ->
            prefs[UserPreferenceKeys.USER_ID] = userId
            prefs[UserPreferenceKeys.USER_NAME] = userName
        }
    }

    suspend fun clearSession() {
        ds.edit { it.clear() }
    }
}
