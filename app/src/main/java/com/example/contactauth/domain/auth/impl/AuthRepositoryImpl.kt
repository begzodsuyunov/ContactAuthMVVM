package com.example.contactauth.domain.auth.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.contactauth.data.local.model.AuthData
import com.example.contactauth.data.local.model.AuthStateEnum
import com.example.contactauth.data.local.model.User
import com.example.contactauth.data.local.model.toRequest
import com.example.contactauth.data.local.room.database.AppDatabase
import com.example.contactauth.data.local.shp.MySharedPreference
import com.example.contactauth.data.server.RetrofitInstance
import com.example.contactauth.domain.auth.AuthRepository
import com.example.contactauth.utils.hasConnection
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AuthRepositoryImpl : AuthRepository {

    private val api = RetrofitInstance.authApi
    private val contactApi = RetrofitInstance.contactApi
    private val dao = AppDatabase.getInstance().contactDao()

    private val localStorage = MySharedPreference.getInstance()

    private val loadingStateLiveData = MutableLiveData(false)
    private val errorMessageLiveData = MutableLiveData<String>()
    private val successMessageLiveData = MutableLiveData<String>()

    companion object {
        private var authRepository: AuthRepository? = null

        fun init() {
            if (authRepository == null)
                authRepository = AuthRepositoryImpl()
        }

        fun instance() = authRepository!!
    }

    private fun readFromData(): ArrayList<User> {
        var list: ArrayList<User> = ArrayList<User>()
        val gson = Gson()
        val result: String = localStorage.users
        val type = object : TypeToken<ArrayList<User>?>() {}.type
        if (result != "") {
            list = gson.fromJson<ArrayList<User>>(result, type)
        }
        return list
    }

    override suspend fun createAccount(authData: AuthData) {
        if (hasConnection()) {
            loadingStateLiveData.postValue(true)
            val response = api.createAccount(authData.toRequest())

            when (response.code()) {
                in 200..299 -> {
                    val token = response.body()?.data?.token

                    localStorage.token = token.toString()

                    val userList = readFromData()
                    val gson = Gson()

                    userList.add(User(token, authData.name, authData.password))
                    localStorage.users = gson.toJson(userList)
                    successMessageLiveData.postValue(response.body()?.message.toString())

                }
                in 400..499 -> {
                    errorMessageLiveData.postValue(response.body()?.message.toString())
                }
                else -> {
                    errorMessageLiveData.postValue(response.body()?.message.toString())
                }
            }

            try {

            } catch (e: Exception) {
                errorMessageLiveData.value = e.message
            }
        } else {
            errorMessageLiveData.value = "No internet"
        }
    }

    override suspend fun deleteAccount() {
        var authData = AuthData("", "")
        val userList = readFromData()

        for (i in userList.indices) {
            if (userList[i].token == localStorage.token) {
                authData = AuthData(userList[i].name, userList[i].password)
            }
        }
        if (hasConnection()) {
            loadingStateLiveData.postValue(true)
            val response = api.deleteAccount(authData.toRequest())

            when (response.code()) {
                in 200..299 -> {

                    dao.delete()
                    val list = readFromData()
                    for (i in list.indices) {
                        if (list[i].name == authData.name && list[i].password == authData.password) {
                            list.removeAt(i)
                            localStorage.token = ""
                            localStorage.isFirst = false
                            successMessageLiveData.value =
                                response.body()?.message.toString()
                        }
                    }

                }
                else -> {
                    errorMessageLiveData.postValue(response.errorBody()?.string().toString())
                }
            }
        } else {
            errorMessageLiveData.postValue("No internet")
        }


    }

    var salom = false

    override suspend fun login(authData: AuthData) {
        if (hasConnection()) {
            loadingStateLiveData.postValue(true)

            val response = api.login(authData.toRequest())


            when (response.code()) {
                in 200..299 -> {
                    val list = readFromData()

                    localStorage.token = response.body()?.data?.token.toString()
                    for (i in list.indices) {
                        if (list[i].name == authData.name && list[i].password == authData.password) {
                            localStorage.token = list[i].token.toString()
                        }
                    }
                    localStorage.isFirst = true
                    salom = true
                    successMessageLiveData.postValue(response.body()?.message.toString())
                }
                else -> {
                    errorMessageLiveData.postValue(response.errorBody()?.string().toString())
                    salom = false
                }
            }
        } else {

            val list = readFromData()

            for (i in list.indices) {
                if (list[i].name == authData.name && list[i].password == authData.password) {
                    successMessageLiveData.value = "Welcome to my channel"
                }
            }
            errorMessageLiveData.postValue("No internet")
        }
    }

    override suspend fun logout() {
        var authData = AuthData("", "")
        var userList = readFromData()


        authData = AuthData(userList[0].name, userList[0].password)

        if (hasConnection()) {
            loadingStateLiveData.postValue(true)

            try {

                val response = api.logout(authData.toRequest())

                when (response.code()) {
                    in 200..299 -> {
                        dao.delete()
                        localStorage.token = ""
                        localStorage.isFirst = false
                        dao.delete()
                        successMessageLiveData.value = response.body()?.message.toString()
                    }
                    else -> {
                        errorMessageLiveData.postValue(response.errorBody()?.string().toString())
                    }
                }
            } catch (e: Exception) {
                errorMessageLiveData.postValue(e.message)
            }
        } else {
            errorMessageLiveData.postValue("No internet")
        }

    }

    override suspend fun checkState(): AuthStateEnum {

        if (hasConnection()) {
            return if (localStorage.token.isNotEmpty()) {
                val request = contactApi.getAllContacts(localStorage.token)
                var state: AuthStateEnum = AuthStateEnum.LOG_UT
                val thread = Thread {
                    val response = request.execute()
                    state = if (response.isSuccessful) {
                        AuthStateEnum.LOGIN
                    } else AuthStateEnum.LOG_UT
                }
                thread.start()
                thread.join()
                state
            } else {
                AuthStateEnum.LOG_UT
            }
        } else {
            return if (localStorage.token.isNotEmpty())
                AuthStateEnum.LOGIN
            else AuthStateEnum.LOG_UT
        }
    }

    override fun loadingState(): MutableLiveData<Boolean> = loadingStateLiveData

    override fun errorMessage(): MutableLiveData<String> = errorMessageLiveData

    override fun successMessage(): MutableLiveData<String> = successMessageLiveData
}