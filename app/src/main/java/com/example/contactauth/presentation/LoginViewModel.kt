package com.example.contactauth.presentation

import androidx.lifecycle.LiveData
import com.example.contactauth.data.local.model.AuthData

interface LoginViewModel {
    val loadingLiveData: LiveData<Boolean>
    val errorMessage: LiveData<String>
    val successLiveData: LiveData<String>

    fun login(authData: AuthData)
}