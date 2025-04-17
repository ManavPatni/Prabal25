package com.devmnv.prabal25.model

data class Coupon(
    val id: Int,
    val couponCode: String,
    val description: String,
    val enabledAt: String,
    val expiresAt: String,
    val isClaimable: Boolean
)

data class CouponsWrapper(
    val coupons: List<Coupon>,
    val status: Int
)
