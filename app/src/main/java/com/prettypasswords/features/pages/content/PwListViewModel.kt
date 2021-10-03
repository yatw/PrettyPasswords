package com.prettypasswords.features.pages.content

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.prettypasswords.globals.PrettyManager
import com.prettypasswords.model.Password

class PwListViewModel: ViewModel() {

    private val _pwList = MutableLiveData<MutableList<Password>?>()
    val pwList: LiveData<MutableList<Password>?> = _pwList


    fun loadPasswords(){
        val pwList = PrettyManager.decrypt()
        _pwList.postValue(pwList)
    }

    fun addPassword(context: Context, password: Password){
        val listValue = _pwList.value

        val list = listValue ?: ArrayList()
        list.add(password)
        PrettyManager.addPassword(context, list)
        _pwList.postValue(list)
    }

}