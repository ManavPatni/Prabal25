package com.devmnv.prabal25.model

data class Team(
    val id: Int,
    val name: String,
    val collegeName: String,
    val address: String,
    val houseId: Int,
    val isPresent: Boolean
)

data class TeamWrapper(
    val team: Team,
    val members: List<User>
)

data class ResponseData(
    val message: String,
    val error: String?,
    val status: Int
)