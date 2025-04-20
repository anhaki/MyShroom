package com.haki.myshroom.database.entity

import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserEntity(
    @PrimaryKey
    @SerialName("id")
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    @SerialName("is_premium")
    val isPremium: Boolean = false,
    @SerialName("exp_date")
    val expDate: String? = null,
    @SerialName("is_admin")
    val isAdmin: Boolean = false,
)