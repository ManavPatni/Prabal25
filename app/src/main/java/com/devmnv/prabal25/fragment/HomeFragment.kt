package com.devmnv.prabal25.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.devmnv.prabal25.adapter.TeamMemberAdapter
import com.devmnv.prabal25.auth.AuthManager
import com.devmnv.prabal25.databinding.FragmentHomeBinding
import com.devmnv.prabal25.network.Services
import com.devmnv.prabal25.sharedPrefs.AuthSharedPref
import com.devmnv.prabaladmin.network.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var teamMemberAdapter: TeamMemberAdapter
    private val apiService by lazy { RetrofitClient.instance.create(Services::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val teamId = AuthManager.getTeamId(requireContext())
        fetchTeamDetails(teamId)

        return binding.root
    }

    private fun fetchTeamDetails(teamId: String) {
        lifecycleScope.launch {
            try {
                val token = AuthManager.getToken(requireContext())
                val response = apiService.getTeam(token, teamId)

                if (response.isSuccessful && response.body() != null) {
                    val teamData = response.body()!!
                    binding.tvTeamName.text = teamData.team.name
                    AuthSharedPref(requireContext()).setHouseId(teamData.team.houseId.toString())
                    AuthSharedPref(requireContext()).setTeamName(teamData.team.name)
                    teamMemberAdapter = TeamMemberAdapter(teamData.members.toMutableList())
                    binding.rvMembers.apply {
                        layoutManager = LinearLayoutManager(requireContext())
                        adapter = teamMemberAdapter
                    }
                    Log.d("TeamDetails", "Team fetched successfully: $teamData")
                } else {
                    val errorCode = response.code()
                    val errorMsg = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("TeamDetails", "API error: $errorCode, $errorMsg")
                    if (errorCode == 401) {
                        AuthManager.logout(requireContext())
                        showToast("Session expired. Please log in again.")
                    } else {
                        showToast("Failed to load team details.")
                    }
                }
            } catch (e: IOException) {
                Log.e("TeamDetails", "Network error: ${e.message}", e)
                showToast("Network error. Please check your connection.")
            } catch (e: HttpException) {
                Log.e("TeamDetails", "HTTP exception: ${e.message}", e)
                showToast("Server error. Please try again later.")
            } catch (e: Exception) {
                Log.e("TeamDetails", "Unexpected error: ${e.message}", e)
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
