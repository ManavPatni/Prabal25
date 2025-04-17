package com.devmnv.prabal25.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.devmnv.prabal25.sharedPrefs.AuthSharedPref
import com.onesignal.OneSignal

object AuthManager {

    fun getToken(context: Context): String {
        val token = "Bearer ${AuthSharedPref(context).token()}"
        Log.d("idToken", token)
        return token
    }

    fun getTeamId(context: Context): String {
        return AuthSharedPref(context).teamId()!!
    }

    fun logout(context: Context) {
        val authSharedPref = AuthSharedPref(context)
        with(authSharedPref) {
            setSignInStatus(false)
            setUID(null)
            setTeamId(null)
            setHouseId(null)
            setName(null)
            setEmail(null)
            setLeaderStatus(false)
            setToken(null)
        }

        OneSignal.logout()

        val intent = Intent(context, SignInActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)

        if (context is Activity) {
            context.finish()
        }
    }

    fun markOnboardingAsComplete(context: Context) {
        AuthSharedPref(context).setonBoardingStatus(true)
    }

}