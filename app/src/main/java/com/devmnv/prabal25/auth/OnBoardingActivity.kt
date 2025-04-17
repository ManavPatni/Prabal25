package com.devmnv.prabal25.auth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.devmnv.prabal25.R
import com.devmnv.prabal25.databinding.ActivityOnBoardingBinding

class OnBoardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnBoardingBinding
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enterFullscreen()

        val videoUri = Uri.parse("android.resource://${packageName}/${R.raw.intro_video}")
        binding.videoView.setVideoURI(videoUri)

        binding.videoView.setOnPreparedListener { mediaPlayer ->
            mediaPlayer.start()

            // Show skip button after 10 seconds
            handler.postDelayed({
                binding.btnSkip.isVisible = true
            }, 10_000)

            // Hide skip button at 25 seconds if not clicked
            handler.postDelayed({
                binding.btnSkip.isVisible = false
            }, 25_000)
        }

        binding.btnSkip.setOnClickListener {
            binding.btnSkip.isVisible = false
            binding.videoView.seekTo(30_000) // skip to 30 seconds
        }

        binding.videoView.setOnCompletionListener {
            AuthManager.markOnboardingAsComplete(this)
            startActivity(Intent(this, SignInActivity::class.java))
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            finish()
        }
    }

    private fun enterFullscreen() {
        // For Android 11+ (API 30+)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            window.insetsController?.let { controller ->
                controller.hide(
                    WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars()
                )
                controller.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }

            // Allow content in display cutout (notch)
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES

        } else {
            // For below Android 11
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility =
                (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

            @Suppress("DEPRECATION")
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
    }

}
