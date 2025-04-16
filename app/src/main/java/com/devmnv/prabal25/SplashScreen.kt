package com.devmnv.prabal25

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.devmnv.prabal25.auth.OnBoardingActivity
import com.devmnv.prabal25.auth.SignInActivity
import com.devmnv.prabal25.databinding.ActivitySplashScreenBinding
import com.devmnv.prabal25.sharedPrefs.AuthSharedPref
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var authSharedPref: AuthSharedPref
    private val splashDelay: Long = 2000L
    private var hasNavigated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authSharedPref = AuthSharedPref(this)

        // Start the delayed navigation
        lifecycleScope.launch {
            delay(splashDelay)
            navigateBasedOnAuthStatus()
        }
    }

    private fun navigateBasedOnAuthStatus() {
        val nextActivity = when {
            !authSharedPref.onBoardingStatus() -> OnBoardingActivity::class.java
            authSharedPref.signInStatus() -> MainActivity::class.java
            else -> SignInActivity::class.java
        }
        exit(nextActivity)
    }

    private fun exit(activityClass: Class<out Activity>) {
        if (hasNavigated) return
        hasNavigated = true
        startActivity(Intent(this, activityClass))
        finishAffinity()
    }

}
