package com.example.contactauth.presentation

import androidx.lifecycle.LiveData
import com.example.contactauth.data.local.model.AuthData

interface RegisterViewModel {
    val loadingLiveData: LiveData<Boolean>
    val errorMessage: LiveData<String>
    val successLiveData: LiveData<String>
    val contactSuccessLiveData: LiveData<String>

    fun createAccount(authData: AuthData)
}