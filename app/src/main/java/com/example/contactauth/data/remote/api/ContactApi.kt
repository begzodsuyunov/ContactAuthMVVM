package com.example.contactauth.data.remote.api

import com.example.contactauth.data.remote.request.ContactRequest
import com.example.contactauth.data.remote.response.BaseResponse
import com.example.contactauth.data.remote.response.ContactResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface ContactApi {
    @GET("contact")
    fun getAllContacts(
        @Header("token") token: String
    ): Call<BaseResponse<List<ContactResponse>>>


    @POST("contact")
    suspend fun addContact(
        @Header("token") token: String,
        @Body request: ContactRequest
    ): Response<BaseResponse<ContactResponse>>

    @PUT("contact")
    suspend fun updateContact(
        @Header("token") token: String,
        @Query("id") id: Int,
        @Body request: ContactRequest
    ): Response<BaseResponse<ContactRequest>>

    @GET("contact")
    fun getContact(
        @Header("token") token: String,
        @Query("id") id: Int
    ): Response<BaseResponse<ContactResponse>>

    @DELETE("contact")
    suspend fun deleteContact(
        @Header("token") token: String,
        @Query("id") id: Int
    ): Response<BaseResponse<ContactResponse>>
}