package com.prettypasswords.features.pages.content

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import timber.log.Timber


class ProfileViewModel: ViewModel() {


    private val _userNameChecked = MutableLiveData(false)
    val userNameChecked: LiveData<Boolean> = _userNameChecked

    private val _updatePasswordChecked = MutableLiveData(false)
    val updatePasswordChecked: LiveData<Boolean> = _updatePasswordChecked

    fun updateUserNameChecked(){
        var checked = _userNameChecked.value?: false
        _userNameChecked.postValue(!checked)
    }

    fun updatePasswordChecked(){
        var checked = _updatePasswordChecked.value?: false
        _updatePasswordChecked.postValue(!checked)
    }

}