package com.example.contactauth.data.remote.response

data class BaseResponse<T>(
    val message: String,
    val data: T?
)