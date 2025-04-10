package com.devmnv.prabal25.auth

import android.content.Context
import com.devmnv.prabal25.sharedPrefs.AuthSharedPref

object AuthManager {

    fun markOnboardingAsComplete(context: Context) {
        AuthSharedPref(context).setOnBoardingStatus(true)
    }

}