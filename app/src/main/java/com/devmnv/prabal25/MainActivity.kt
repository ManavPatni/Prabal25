package com.devmnv.prabal25

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.devmnv.prabal25.auth.AuthManager
import com.devmnv.prabal25.databinding.ActivityMainBinding
import com.devmnv.prabal25.fragment.CouponFragment
import com.devmnv.prabal25.fragment.HomeFragment
import com.devmnv.prabal25.fragment.PassFragment
import com.devmnv.prabal25.sharedPrefs.AuthSharedPref

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var authSharedPref: AuthSharedPref

    private var homeFragment: HomeFragment? = null
    private var passFragment: PassFragment? = null
    private var couponFragmentTag = "coupon_fragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Initialization
        authSharedPref = AuthSharedPref(this)
        //OneSignal

        // Init fragments
        homeFragment = HomeFragment()
        passFragment = PassFragment()

        // Load default fragment
        supportFragmentManager.beginTransaction()
            .add(R.id.frameLayout, homeFragment!!, "home")
            .commit()

        setupBottomNav()
    }

    private fun setupBottomNav() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    showFragment(homeFragment!!)
                    true
                }
                R.id.nav_pass -> {
                    showFragment(passFragment!!)
                    true
                }
                R.id.nav_coupon -> {
                    // Always refresh CouponFragment
                    val couponFragment = CouponFragment()
                    replaceFragment(couponFragment, couponFragmentTag)
                    true
                }
                R.id.nav_logout -> {
                    AuthManager.logout(this)
                    true
                }
                else -> false
            }
        }
    }

    private fun showFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()

        // Hide all existing fragments
        for (frag in supportFragmentManager.fragments) {
            transaction.hide(frag)
        }

        // Add or show the selected fragment
        if (!fragment.isAdded) {
            transaction.add(R.id.frameLayout, fragment)
        } else {
            transaction.show(fragment)
        }

        transaction.commit()
    }

    private fun replaceFragment(fragment: Fragment, tag: String) {
        val transaction = supportFragmentManager.beginTransaction()

        // Remove old coupon fragment if it exists
        val oldCoupon = supportFragmentManager.findFragmentByTag(tag)
        if (oldCoupon != null) {
            transaction.remove(oldCoupon)
        }

        transaction.add(R.id.frameLayout, fragment, tag)

        // Hide all other fragments
        for (frag in supportFragmentManager.fragments) {
            if (frag != fragment) transaction.hide(frag)
        }

        transaction.commit()
    }
}
