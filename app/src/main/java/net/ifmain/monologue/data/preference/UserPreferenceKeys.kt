package net.ifmain.monologue.data.preference

import androidx.datastore.preferences.core.stringPreferencesKey

object UserPreferenceKeys {
    val USER_ID   = stringPreferencesKey("user_id")
    val USER_NAME = stringPreferencesKey("user_name")
}
