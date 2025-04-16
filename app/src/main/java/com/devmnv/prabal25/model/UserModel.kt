package com.devmnv.prabal25.model

data class LoginRequest(
    val username: String,
    val password: String
)

data class User(
    val id: Int,
    val teamId: Int,
    val name: String,
    val gender: String,
    val email: String,
    val phoneNumber: String,
    val isLeader: Boolean,
    val token: String
)

