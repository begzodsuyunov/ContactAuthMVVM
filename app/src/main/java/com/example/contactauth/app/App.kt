package com.example.contactauth.app

import android.app.Application
import com.example.contactauth.data.local.room.database.AppDatabase
import com.example.contactauth.data.local.shp.MySharedPreference
import com.example.contactauth.domain.auth.impl.AuthRepositoryImpl
import com.example.contactauth.domain.contact.impl.ContactRepositoryImpl

class App : Application() {

    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        MySharedPreference.init(this)
        AppDatabase.init(this)
        AuthRepositoryImpl.init()
        ContactRepositoryImpl.init()

        instance = this
    }

}