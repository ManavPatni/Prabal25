package com.devmnv.prabal25.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.devmnv.prabal25.MainActivity
import com.devmnv.prabal25.R
import com.devmnv.prabal25.databinding.ActivitySignInBinding
import com.devmnv.prabal25.model.LoginRequest
import com.devmnv.prabal25.model.User
import com.devmnv.prabal25.network.Services
import com.devmnv.prabal25.sharedPrefs.AuthSharedPref
import com.devmnv.prabaladmin.network.RetrofitClient
import com.onesignal.OneSignal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var authSharedPref: AuthSharedPref
    private val apiService by lazy { RetrofitClient.instance.create(Services::class.java) }

    val ONESIGNAL_APP_ID = "cef228d9-7495-497f-9f66-c025472cc8ea"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authSharedPref = AuthSharedPref(this)

        OneSignal.initWithContext(this, ONESIGNAL_APP_ID)
        CoroutineScope(Dispatchers.Main).launch {
            OneSignal.Notifications.requestPermission(true)
        }

        binding.btnLogin.setOnClickListener {
            if (isValidInput()) {
                login()
            }
        }
    }

    private fun isValidInput(): Boolean {
        val username = binding.etUsername.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        return when {
            username.isEmpty() -> {
                binding.etUsername.error = "Username required"
                false
            }

            password.isEmpty() -> {
                binding.etPassword.error = "Password required"
                false
            }

            else -> true
        }
    }

    private fun login() {
        val request = LoginRequest(
            username = binding.etUsername.text.toString().trim(),
            password = binding.etPassword.text.toString().trim()
        )

        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiService.login(request)
                }

                if (response.isSuccessful && response.body() != null) {
                    val user = response.body()!!
                    updateSharedPref(user)
                    showToast("Welcome ${user.name} 👋👋!!")
                    startActivity(Intent(this@SignInActivity, MainActivity::class.java))
                    Log.d("data",response.body().toString())
                    finish()
                } else {
                    val errorMsg = "Login failed (${response.code()})"
                    Log.e("SignIn", "API Error: ${response.errorBody()?.string()}")
                    showToast(errorMsg)
                }
            } catch (e: IOException) {
                Log.e("SignIn", "Network Error", e)
                showToast("Network issue (IO)")
            } catch (e: HttpException) {
                Log.e("SignIn", "HTTP Exception: Code ${e.code()} - ${e.message()}", e)
                showToast("Server issue (${e.code()})")
            } catch (e: Exception) {
                Log.e("SignIn", "Unknown error", e)
                showToast("Something wrong (?)")
            }
        }
    }

    private fun updateSharedPref(user: User) {
        with(authSharedPref) {
            setSignInStatus(true)
            setUID(user.id.toString())
            setTeamId(user.teamId.toString())
            setName(user.name)
            setEmail(user.email)
            setLeaderStatus(user.isLeader)
            setToken(user.token)
        }
        OneSignal.login(externalId = user.id.toString())
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
