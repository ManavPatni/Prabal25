package com.devmnv.prabal25.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
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

        // Clear OneSignal's user and subscription data
        OneSignal.logout()

        // Clear local user data
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

        // Optional: Wait briefly if needed to ensure OneSignal clears user before relaunching
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(context, SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)

            if (context is Activity) {
                context.finish()
            }
        }, 500) // Small delay to let OneSignal process logout (can be tuned)
    }


    fun markOnboardingAsComplete(context: Context) {
        AuthSharedPref(context).setonBoardingStatus(true)
    }

}