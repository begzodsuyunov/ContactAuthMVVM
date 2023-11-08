package com.example.contactauth.presentation.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactauth.data.local.model.AuthData
import com.example.contactauth.domain.auth.impl.AuthRepositoryImpl
import com.example.contactauth.presentation.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModelImpl : LoginViewModel, ViewModel() {
    private val repository = AuthRepositoryImpl()



    override val loadingLiveData: MutableLiveData<Boolean> = repository.loadingState()
    override val errorMessage: MutableLiveData<String> = repository.errorMessage()

    override val successLiveData: MutableLiveData<String> = repository.successMessage()


    override fun login(authData: AuthData) {
        viewModelScope.launch(Dispatchers.IO) {
            if (authData.name.isNotEmpty() && authData.password.isNotEmpty()) {

                if (authData.password.length > 5) {
                    repository.login(authData)
                } else {
                    errorMessage.postValue("Parol 5 ta belgidan ko'p bo'lishi shart")
                }
            } else {
                errorMessage.postValue("Maydonlar bo'sh")
            }
        }
    }
}