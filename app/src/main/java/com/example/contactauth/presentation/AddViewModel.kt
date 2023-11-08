package com.example.contactauth.presentation

import androidx.lifecycle.LiveData
import com.example.contactauth.data.local.room.entity.ContactEntity

interface AddViewModel {

    val errorLiveData: LiveData<String>

    val successLiveData: LiveData<String>

    val openScreenLiveData: LiveData<Unit>

    fun add(contactEntity: ContactEntity)
}