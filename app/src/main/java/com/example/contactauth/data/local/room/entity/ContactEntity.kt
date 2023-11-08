package com.example.contactauth.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "contact")
data class ContactEntity(
    @PrimaryKey
    var id: String,
    var name: String,
    var phone: String,
    var statusAdd: Int,
    var statusUpdate: Int,
    var statusDelete: Int
): Serializable