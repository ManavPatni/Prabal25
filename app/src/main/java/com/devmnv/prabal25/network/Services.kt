package com.devmnv.prabal25.network

import com.devmnv.prabal25.model.LoginRequest
import com.devmnv.prabal25.model.TeamWrapper
import com.devmnv.prabal25.model.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface Services {

    //Config
    @GET("api/config")
    fun registerUser(): Call<ResponseBody>

    //User Login
    @POST("team/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<User>

    //Get specific team
    @GET("team/{teamId}")
    suspend fun getTeam(
        @Header("Authorization") token: String,
        @Path("teamId") teamId: String
    ): Response<TeamWrapper>

}