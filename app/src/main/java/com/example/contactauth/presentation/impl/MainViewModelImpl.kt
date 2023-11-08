package com.example.contactauth.presentation.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactauth.data.local.room.entity.ContactEntity
import com.example.contactauth.domain.auth.impl.AuthRepositoryImpl
import com.example.contactauth.domain.contact.impl.ContactRepositoryImpl
import com.example.contactauth.presentation.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModelImpl : MainViewModel, ViewModel() {

    private val repository = ContactRepositoryImpl()
    private val repositoryAuth = AuthRepositoryImpl()


    override val errorLiveData: LiveData<String> = repository.errorMessage()
    override val successLiveData: LiveData<String> = repository.successMessage()
    override val listLiveData: MediatorLiveData<List<ContactEntity>> = MediatorLiveData()

    override fun sync() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.sync()
        }
    }

    override fun delete(contactEntity: ContactEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(contactEntity)
        }
    }

    override fun deleteAccount() {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryAuth.deleteAccount()
        }
    }

    override fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryAuth.logout()
        }
    }

    init {
        listLiveData.addSource(repository.getAllContact()) {
            listLiveData.postValue(it)
        }
    }
}