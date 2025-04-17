package com.devmnv.prabal25.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.devmnv.prabal25.adapter.CouponAdapter
import com.devmnv.prabal25.auth.AuthManager
import com.devmnv.prabal25.databinding.FragmentCouponBinding
import com.devmnv.prabal25.network.Services
import com.devmnv.prabaladmin.network.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class CouponFragment : Fragment() {

    private var _binding: FragmentCouponBinding? = null
    private val binding get() = _binding!!
    private lateinit var couponAdapter: CouponAdapter
    private val apiService by lazy { RetrofitClient.instance.create(Services::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCouponBinding.inflate(inflater, container, false)
        fetchCoupons()
        return binding.root
    }

    private fun fetchCoupons() {
        lifecycleScope.launch {
            try {
                val token = AuthManager.getToken(requireContext())
                val response = apiService.getAvailableCoupons(token)

                if (response.isSuccessful && response.body() != null) {
                    val couponData = response.body()!!
                    couponAdapter = CouponAdapter(couponData.coupons.toMutableList())
                    binding.rvCoupons.apply {
                        layoutManager = LinearLayoutManager(requireContext())
                        adapter = couponAdapter
                    }
                    if (couponAdapter.itemCount == 0) binding.tvNoCouponsFound.visibility = View.VISIBLE
                    Log.d("CouponFragment", "Coupons fetched successfully: $couponData")
                } else {
                    binding.tvNoCouponsFound.visibility = View.VISIBLE
                    val errorCode = response.code()
                    val errorMsg = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("CouponFragment", "API error: $errorCode, $errorMsg")
                    if (errorCode == 401) {
                        AuthManager.logout(requireContext())
                        showToast("Session expired. Please log in again.")
                    } else {
                        showToast("Failed to load coupons details.")
                    }
                }
            } catch (e: IOException) {
                Log.e("CouponFragment", "Network error: ${e.message}", e)
                showToast("Network error. Please check your connection.")
            } catch (e: HttpException) {
                Log.e("CouponFragment", "HTTP exception: ${e.message}", e)
                showToast("Server error. Please try again later.")
            } catch (e: Exception) {
                Log.e("CouponFragment", "Unexpected error: ${e.message}", e)
                showToast("An unexpected error occurred.")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}