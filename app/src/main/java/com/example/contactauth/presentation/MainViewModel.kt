package com.example.contactauth.presentation

import androidx.lifecycle.LiveData
import com.example.contactauth.data.local.room.entity.ContactEntity

interface MainViewModel {


    val errorLiveData: LiveData<String>

    val successLiveData: LiveData<String>

    val listLiveData: LiveData<List<ContactEntity>>

    fun sync()

    fun delete(contactEntity: ContactEntity)

    fun deleteAccount()

    fun logout()
}