package com.example.contactauth.data.remote.api

import com.example.contactauth.data.remote.request.AuthRequest
import com.example.contactauth.data.remote.response.AuthResponse
import com.example.contactauth.data.remote.response.BaseResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("register")
    suspend fun createAccount(
        @Body authRequest: AuthRequest
    ): Response<BaseResponse<AuthResponse>>

    @POST("unregister")
    suspend fun deleteAccount(
        @Body authRequest: AuthRequest
    ): Response<BaseResponse<AuthResponse>>

    @POST("login")
    suspend fun login(
        @Body authRequest: AuthRequest
    ): Response<BaseResponse<AuthResponse>>

    @POST("logout")
    suspend fun logout(
        @Body authRequest: AuthRequest
    ): Response<BaseResponse<AuthResponse>>

}