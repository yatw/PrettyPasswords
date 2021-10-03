package com.prettypasswords.features.pages.content

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.prettypasswords.globals.PrettyManager
import com.prettypasswords.model.Password
import java.lang.IllegalStateException

class PwViewModel: ViewModel() {

    private val _pwList by lazy {
        val liveData = MutableLiveData<MutableList<Password>>()
        liveData.postValue(PrettyManager.decrypt())
        return@lazy liveData
    }
    val pwList: LiveData<MutableList<Password>> = _pwList

    fun addPassword(context: Context, password: Password){
        val list = _pwList.value?: ArrayList()
        list.add(password)
        PrettyManager.savePasswords(context, list)
        _pwList.postValue(list)
    }

    fun getPassword(index: Int): Password{
        val list = _pwList.value?: throw IllegalStateException("passwordList is null")
        return list[index]
    }

    // no need to update liveData because it is the same list
    fun updatePassword(context: Context, index: Int, password: Password){
        val list = _pwList.value?: throw IllegalStateException("passwordList is null")
        list.set(index, password)
        PrettyManager.savePasswords(context, list)
    }

    // no need to update liveData because it is the same list
    fun deletePassword(context: Context, index: Int){
        val list = _pwList.value?: throw IllegalStateException("passwordList is null")
        list.removeAt(index)
        PrettyManager.savePasswords(context, list)
    }

}