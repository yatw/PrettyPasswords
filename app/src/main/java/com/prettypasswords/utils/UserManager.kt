package com.prettypasswords.utils

import android.content.Context
import com.prettypasswords.globals.PrettyManager
import com.prettypasswords.model.Credential
import timber.log.Timber
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.util.*

/*
    This class handle user related operation and credential
 */
class UserManager {

    //initialized during createUser or restoreCredentialFromFile
    //having credential doesn't mean user is login, having sk means user is login
    var credential: Credential? = null

    private var sak: ByteArray? = null

    companion object {

        fun createCredential(context: Context, userName: String, password: String): Credential{

            val userKeyPair : Array<ByteArray> = PrettyManager.e.generateAKey()

            val pk = userKeyPair[0]
            val sk = userKeyPair[1]

            val esk: ByteArray = PrettyManager.e.generateESKey(sk, password)
            val xesak = PrettyManager.e.generateXESAK(pk, sk)


            return Credential(
                userName = userName,
                pk = pk,
                sk = sk,
                esk = esk,
                xesak = xesak
            )
        }

    }

    fun initWithCredential(context: Context, c: Credential){

        val sk = c.getSk()?: throw IllegalArgumentException("missing sk")

        // decrypt sak from xesak
        sak = PrettyManager.e.generateSAK(c.xesak, c.pk, sk)
        saveCurrentUserName(context, c.userName)
        this.credential = c
    }


    // use after user has credential
    fun loginByPassword(context: Context, password: String): Boolean{

        val c = credential ?: throw IllegalStateException("credential is null")
        val esk: ByteArray = c.esk
        val pk: ByteArray = c.pk

        val decryptedSK: ByteArray = PrettyManager.e.decryptESKtoSK(esk,  password)
        val generatedpk: ByteArray = PrettyManager.e.generatePk(decryptedSK)


        // if not equals, incorrect password
        if (!generatedpk.contentEquals(pk)){
            return false
        }

        c.setSk(decryptedSK)

        // decrypt sak from xesak
        sak = PrettyManager.e.generateSAK(c.xesak, pk, decryptedSK)
        saveCurrentUserName(context, c.userName)

        return true
    }

    fun checkPasswordCorrect(password: String): Boolean{
        val c = credential ?: throw IllegalStateException("credential is null")
        val esk: ByteArray = c.esk
        val pk: ByteArray = c.pk

        val decryptedSK: ByteArray = PrettyManager.e.decryptESKtoSK(esk,  password)
        val generatedpk: ByteArray = PrettyManager.e.generatePk(decryptedSK)

        // if not equals, incorrect password
        return Arrays.equals(generatedpk,pk)
    }


    fun getUserName(): String{
        return credential?.userName ?: throw IllegalStateException("null credential")
    }


    // save the userName in sharedpreference on create and on login
    fun saveCurrentUserName(context: Context, userName: String){

        val sharedPref = context.getSharedPreferences(PrettyManager.sharedPreferenceKey, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        editor.putString("userName", userName)
        editor.commit()
    }


    fun getLastSessionUserName(context: Context): String {
        val sharedPref = context.getSharedPreferences(PrettyManager.sharedPreferenceKey, Context.MODE_PRIVATE)
        val userName = sharedPref.getString("userName","")
        return userName ?: ""
    }

    fun signout(context: Context){
        // this is hard logout, erase credential
        // but for this app, when signin hasContential() will restore
        credential = null
    }

    fun logout(context: Context){
        // This is normal logout, still have credential
        val c = credential?: throw IllegalStateException("credential is null")
        c.setSk(null)
    }


}