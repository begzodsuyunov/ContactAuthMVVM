package com.example.contactauth.data.local.model

import com.example.contactauth.data.remote.request.AuthRequest

data class AuthData(
    val name: String,
    val password:String
)

fun AuthData.toRequest() = AuthRequest(name, password)