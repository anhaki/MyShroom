package com.haki.myshroom.preferences

data class UserPrefModel(
    val id: String,
    val name: String = "",
    val email: String,
    val isPremium: Boolean = false,
    val token: String?,
    val isLogin: Boolean = false,
    val isAdmin: Boolean = false,
)