package com.example.contactauth.data.local.model

data class User(
    var token: String? = null,
    val name: String,
    val password: String
)
