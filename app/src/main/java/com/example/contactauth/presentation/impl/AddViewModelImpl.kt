package com.example.contactauth.presentation.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactauth.data.local.room.entity.ContactEntity
import com.example.contactauth.domain.contact.impl.ContactRepositoryImpl
import com.example.contactauth.presentation.AddViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddViewModelImpl : AddViewModel, ViewModel(){

    private val repository = ContactRepositoryImpl()

    override val errorLiveData: LiveData<String> = repository.errorMessage()
    override val successLiveData: LiveData<String> = repository.successMessage()

    override val openScreenLiveData: MutableLiveData<Unit> = MutableLiveData()

    override fun add(contactEntity: ContactEntity) {

        contactEntity.phone =
            contactEntity.phone.replace(" ", "").replace("-", "").replace("(", "").replace(")", "")

        viewModelScope.launch(Dispatchers.IO) {
            repository.addContact(contactEntity)
            openScreenLiveData.postValue(Unit)
        }
    }
}