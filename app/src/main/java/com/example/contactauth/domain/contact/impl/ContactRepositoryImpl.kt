package com.example.contactauth.domain.contact.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.contactauth.data.local.room.database.AppDatabase
import com.example.contactauth.data.local.room.entity.ContactEntity
import com.example.contactauth.data.local.shp.MySharedPreference
import com.example.contactauth.data.remote.request.ContactRequest
import com.example.contactauth.data.remote.response.BaseResponse
import com.example.contactauth.data.remote.response.ContactResponse
import com.example.contactauth.data.server.RetrofitInstance
import com.example.contactauth.domain.contact.ContactRepository
import com.example.contactauth.utils.hasConnection
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContactRepositoryImpl : ContactRepository {

    private val errorMessageLiveData = MutableLiveData<String>()
    private val successMessageLiveData = MutableLiveData<String>()
    private val dao = AppDatabase.getInstance().contactDao()
    private val api = RetrofitInstance.contactApi
    private val sharedPreference = MySharedPreference.getInstance()

    companion object {
        private var contactRepository: ContactRepository? = null

        fun init() {
            if (contactRepository == null)
                contactRepository = ContactRepositoryImpl()
        }

        fun instance() = contactRepository!!
    }


    override suspend fun addContact(contactEntity: ContactEntity) {

        if (hasConnection()) {


            try {
                val response = api.addContact(
                    sharedPreference.token,
                    ContactRequest(contactEntity.name, contactEntity.phone)
                )
                when (response.code()) {
                    in 200..299 -> {
                        val data = response.body()?.data
                        dao.insert(
                            ContactEntity(
                                data?.id.toString(),
                                contactEntity.name,
                                contactEntity.phone,
                                0,
                                0,
                                0
                            )
                        )
                        successMessageLiveData.value = "Added"
                    }

                    else -> {
                        errorMessageLiveData.postValue(response.errorBody()?.string())
                    }
                }

            } catch (e: Exception) {

            }
        } else {
            contactEntity.statusAdd = 1
            errorMessageLiveData.postValue(
                "There is no internet yet, this contact will be uploaded to the server after you add the internet"
            )
            dao.insert(contactEntity)
        }
    }

    override suspend fun update(contactEntity: ContactEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun sync() {
        val list = getAllContacts()
        if (hasConnection()) {
            for (i in list.indices) {
                if (list[i].statusAdd == 1) {
                    addContact(list[i])
                } else if (list[i].statusUpdate == 1) {
                    update(list[i])
                } else if (list[i].statusDelete == 1) {
                    delete(list[i])
                }
            }
            getAllContact()
        } else {
            errorMessageLiveData.postValue("Internet yo'q")
        }
    }

    override suspend fun delete(contactEntity: ContactEntity) {
        if (hasConnection()) {
            try {

                val response = api.deleteContact(sharedPreference.token, contactEntity.id.toInt())
                when (response.code()) {
                    in 200..299 -> {
                        val data = response.body()?.data

                        contactEntity.statusAdd = 0
                        dao.delete(contactEntity)
                        successMessageLiveData.value = "Deleted"
                    }
                    else -> {
                        errorMessageLiveData.postValue(response.errorBody()?.string())
                    }
                }

            } catch (e: Exception) {
                errorMessageLiveData.postValue(e.message)
            }
        } else {
            errorMessageLiveData.postValue(
                "There is no internet yet, this contact will be deleted to the server after you add the internet"
            )
            contactEntity.statusDelete = 1
            dao.update(contactEntity)
        }
    }

    override fun getAllContact(): LiveData<List<ContactEntity>> {
        if (hasConnection()) {
            api.getAllContacts(sharedPreference.token)
                .enqueue(object : Callback<BaseResponse<List<ContactResponse>>> {
                    override fun onResponse(
                        call: Call<BaseResponse<List<ContactResponse>>>,
                        response: Response<BaseResponse<List<ContactResponse>>>
                    ) {

                        when (response.code()) {
                            in 200..299 -> {
                                val list = response.body()!!.data

                                val newList = ArrayList<ContactEntity>()
                                for (i in 0 until list?.size!!) {
                                    newList.add(
                                        ContactEntity(
                                            list[i].id.toString(),
                                            list[i].name,
                                            list[i].phone,
                                            0,
                                            0,
                                            0
                                        )
                                    )
                                }

                                dao.delete()
                                dao.insert(newList)
                                successMessageLiveData.value = "Yangilandi"
                            }
                            else -> {
                                errorMessageLiveData.postValue(response.body()?.message.toString())
                            }
                        }
                    }

                    override fun onFailure(
                        call: Call<BaseResponse<List<ContactResponse>>>,
                        t: Throwable
                    ) {
                        errorMessageLiveData.postValue(t.message)
                    }

                })
        }
        return dao.getAllContact()
    }

    override fun getAllContacts(): List<ContactEntity> = dao.getAllContacts()


    override fun errorMessage(): LiveData<String> = errorMessageLiveData

    override fun successMessage(): LiveData<String> = successMessageLiveData
}