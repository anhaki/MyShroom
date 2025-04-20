package com.haki.myshroom.database.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val code: Long,
    @SerialName("error_code")
    val errorCode: String,
    @SerialName("msg")
    val msg: String,
)