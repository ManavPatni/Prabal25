package com.devmnv.prabal25.sharedPrefs

import android.content.Context
import android.content.SharedPreferences

class AuthSharedPref(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        //Shared Pref keys
        private val PREF_NAME = "AuthSharedPref"

        //Flow Status Key
        private const val KEY_IS_BOARDING_SCREEN_SHOWN = "isBoardingScreenShown"
        private const val KEY_IS_SIGNED_IN = "isSignedIn"

        //User Data Key
        private const val KEY_TEAM_ID = "teamId"
    }

    //Generic method to save a value
    private inline fun <reified T> setValue(key: String, value: T) {
        sharedPreferences.edit().apply{
            when(value) {
                is String? -> putString(key, value)
                is Boolean -> putBoolean(key, value)
                is Int -> putInt(key, value)
                is Float -> putFloat(key, value)
                is Long -> putLong(key, value)
                else -> throw IllegalArgumentException("Unsupported type")
            }
            apply()
        }
    }

    //Generic method to retrieve a value with a default fallback
    private inline fun<reified T> getValue(key: String, defaultValue: T): T {
        return with(sharedPreferences) {
            when (T::class) {
                String::class -> getString(key, defaultValue as? String) as T
                Boolean::class -> getBoolean(key, defaultValue as Boolean) as T
                Int::class -> getInt(key, defaultValue as Int) as T
                Float::class -> getFloat(key, defaultValue as Float) as T
                Long::class -> getLong(key, defaultValue as Long) as T
                else -> throw IllegalArgumentException("Unsupported type")
            }
        }
    }

    //setter functions
    fun setOnBoardingStatus(status: Boolean) = setValue(KEY_IS_BOARDING_SCREEN_SHOWN, status)
    fun setSignInStatus(status: Boolean) = setValue(KEY_IS_SIGNED_IN, status)

    //user details
    fun setUserData(
        teamId: String
    ) {
        setValue(KEY_TEAM_ID, teamId)
    }

    // getter functions
    fun boardingStatus(): Boolean = getValue(KEY_IS_BOARDING_SCREEN_SHOWN, false)
    fun isSignInStatus(): Boolean = getValue(KEY_IS_SIGNED_IN, false)
    fun teamId(): String? = getValue(KEY_TEAM_ID, null)

}