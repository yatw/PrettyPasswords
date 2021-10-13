package com.yatw.prettypasswords.features.pages.content

import android.content.Context
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yatw.prettypasswords.globals.PrettyManager


class ProfileViewModel: ViewModel() {


    val userNameChecked = MutableLiveData(false)

    val userNameInput = MutableLiveData("")

    val passwordChecked = MutableLiveData(false)

    val passwordNow = MutableLiveData("")
    val password1 = MutableLiveData("")
    val password2 = MutableLiveData("")


    val saveBtnEnable = MediatorLiveData<Boolean>().apply {
        this.addSource(userNameChecked){
            this.postValue(shouldSaveBtnEnable())
        }
        this.addSource(userNameInput){
            this.postValue(shouldSaveBtnEnable())
        }
        this.addSource(passwordChecked){
            this.postValue(shouldSaveBtnEnable())
        }
        this.addSource(passwordNow){
            this.postValue(shouldSaveBtnEnable())
        }
        this.addSource(password1){
            this.postValue(shouldSaveBtnEnable())
        }
        this.addSource(password2){
            this.postValue(shouldSaveBtnEnable())
        }
    }


    fun shouldSaveBtnEnable(): Boolean{
        if (userNameChecked.value != true && passwordChecked.value != true){
            return false
        }
        return isUserNameOk() && isPasswordOk()
    }

    fun isUserNameOk(): Boolean{
        val checked = userNameChecked.value?: return false
        return !(checked && userNameInput.value.isNullOrBlank())
    }

    fun isPasswordOk(): Boolean{
        val checked = passwordChecked.value?: return false
        if (!checked){
            return true
        }
        val currentPassword = passwordNow.value
        val pw1 = password1.value
        val pw2 = password2.value
        if (currentPassword.isNullOrBlank() || pw1.isNullOrBlank() || pw2.isNullOrBlank()){
            return false
        }
        if (pw1 != pw2){
            return false
        }
        return true
    }


    fun handleSave(context: Context): Boolean{
        val newUserName = if (userNameChecked.value == true) userNameInput.value else null
        val newPw = if (passwordChecked.value == true) password1.value else null
        return PrettyManager.updateProfile(context, newUserName, newPw)
    }

}