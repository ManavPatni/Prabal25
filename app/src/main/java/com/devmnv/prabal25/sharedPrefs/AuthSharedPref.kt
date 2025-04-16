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
        private const val KEY_ON_BOARDING_COMPLETED = "isOnBoardingCompleted"
        private const val KEY_IS_SIGNED_IN = "isSignedIn"

        //User Data Key
        private const val KEY_UID = "uid"
        private const val KEY_TEAM_ID = "teamId"
        private const val KEY_HOUSE_ID = "houseId"
        private const val KEY_NAME = "name"
        private const val KEY_EMAIL = "email"
        private const val KEY_PHONE_NUMBER = "phoneNumber"
        private const val KEY_IS_LEADER = "isLeader"
        private const val KEY_TOKEN = "token"
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
    fun setonBoardingStatus(status: Boolean) = setValue(KEY_ON_BOARDING_COMPLETED, status)
    fun setSignInStatus(status: Boolean) = setValue(KEY_IS_SIGNED_IN, status)

    //user details
    fun setUID(uid: String?) = setValue(KEY_UID, uid)
    fun setTeamId(teamId: String?) = setValue(KEY_TEAM_ID, teamId)
    fun setHouseId(houseId: String?) = setValue(KEY_HOUSE_ID, houseId)
    fun setName(name: String?) = setValue(KEY_NAME, name)
    fun setEmail(email: String?) = setValue(KEY_EMAIL, email)
    fun setPhoneNumber(phoneNumber: String?) = setValue(KEY_PHONE_NUMBER, phoneNumber)
    fun setLeaderStatus(isLeader: Boolean) = setValue(KEY_IS_LEADER, isLeader)
    fun setToken(token: String?) = setValue(KEY_TOKEN, token)

    // getter functions
    fun onBoardingStatus(): Boolean = getValue(KEY_ON_BOARDING_COMPLETED, false)
    fun signInStatus(): Boolean = getValue(KEY_IS_SIGNED_IN, false)
    fun uid(): String? = getValue(KEY_UID, null)
    fun teamId(): String? = getValue(KEY_TEAM_ID, null)
    fun houseId(): String? = getValue(KEY_HOUSE_ID, null)
    fun name(): String? = getValue(KEY_NAME, null)
    fun email(): String? = getValue(KEY_EMAIL, null)
    fun phoneNumber(): String? = getValue(KEY_PHONE_NUMBER, null)
    fun isLeader(): Boolean = getValue(KEY_IS_LEADER, false)
    fun token(): String? = getValue(KEY_TOKEN, null)

}