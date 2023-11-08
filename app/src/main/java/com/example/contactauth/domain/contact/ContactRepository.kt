package com.example.contactauth.domain.contact

import androidx.lifecycle.LiveData
import com.example.contactauth.data.local.room.entity.ContactEntity

interface ContactRepository {

    suspend fun addContact(contactEntity: ContactEntity)

    suspend fun update(contactEntity: ContactEntity)

    suspend fun sync()

    suspend fun delete(contactEntity: ContactEntity)

    fun getAllContact(): LiveData<List<ContactEntity>>
    fun getAllContacts(): List<ContactEntity>

    fun errorMessage(): LiveData<String>
    fun successMessage(): LiveData<String>



}