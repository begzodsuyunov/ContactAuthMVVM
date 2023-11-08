package com.example.contactauth.domain.auth

import androidx.lifecycle.LiveData
import com.example.contactauth.data.local.model.AuthData
import com.example.contactauth.data.local.model.AuthStateEnum

interface AuthRepository {

    suspend fun createAccount(authData: AuthData)
    suspend fun deleteAccount()
    suspend fun login(authData: AuthData)
    suspend fun logout()

    suspend fun checkState(): AuthStateEnum

    fun loadingState(): LiveData<Boolean>
    fun errorMessage(): LiveData<String>
    fun successMessage(): LiveData<String>

}