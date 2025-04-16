package com.devmnv.prabal25

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.devmnv.prabal25.databinding.ActivityMainBinding
import com.devmnv.prabal25.fragment.HomeFragment
import com.devmnv.prabal25.sharedPrefs.AuthSharedPref

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var authSharedPref: AuthSharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Init sharedPrefs
        authSharedPref = AuthSharedPref(this)

        // Default fragment
        replaceFragment(HomeFragment())


    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .commit()
    }

}