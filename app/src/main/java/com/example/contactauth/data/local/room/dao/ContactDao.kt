package com.example.contactauth.data.local.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.contactauth.data.local.room.entity.ContactEntity

@Dao
interface ContactDao : BaseDao<ContactEntity>{
    @Query("Select * From contact Where statusDelete = 0")
    fun getAllContact(): LiveData<List<ContactEntity>>

    @Query("Select * From contact")
    fun getAllContacts(): List<ContactEntity>
}