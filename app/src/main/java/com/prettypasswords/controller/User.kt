package com.prettypasswords.controller


import android.content.Context
import android.util.Log
import com.prettypasswords.PrettyManager
import com.prettypasswords.model.deleteCryptoFile
import java.util.*


fun getUserName(context: Context): String? {

    val sharedPref = context.getSharedPreferences(PrettyManager.sharedPreferenceKey, Context.MODE_PRIVATE)

    return sharedPref.getString("userName","")
}

fun hasCredential(context: Context, userName: String): Boolean{

    if (PrettyManager.c == null){
        restoreCredentialFromFile(
            context,
            userName
        )
    }

    val credential = PrettyManager.c
    return credential != null && credential.userName.equals(userName)

}

// register, to create cryptoFile
fun createUser(context: Context, userName: String, password: String){

    val userKeyPair : Array<ByteArray> = PrettyManager.e.generateAKey()

    val pk = userKeyPair[0]
    val sk = userKeyPair[1]

    val esk: ByteArray = PrettyManager.e.generateESKey(sk, password)

    val credential = Credential(
        userName = userName,
        pk = pk,
        sk = sk,
        esk = esk
    )
    PrettyManager.c = credential
    PrettyManager.cm = ContentManager()
    PrettyManager.cm!!.saveContentToDisk(context)

    credential.saveUserName(context)

}

// use after user has credential
fun loginByPassword(context: Context, userName: String, password: String): Boolean{

    // must have credential to login
    var credential = PrettyManager.c!!

    val esk: ByteArray = credential.esk
    val pk: ByteArray = credential.pk

    val decryptedSK: ByteArray = PrettyManager.e.decryptESKtoSK(esk,  password)
    val generatedpk: ByteArray = PrettyManager.e.generatePk(decryptedSK)


    // if not equals, incorrect password
    if (!Arrays.equals(generatedpk,pk)){
        return false
    }

    credential.setSk(decryptedSK)
    credential.saveUserName(context)
    PrettyManager.cm!!.body.decrypt(pk, decryptedSK)

    return true
}

fun logout(context: Context){

    val credential = PrettyManager.c;
    if (credential != null){
        credential.clear(context)
    }

    PrettyManager.c = null
}

fun deleteUser(context: Context, userName: String){


    val credential = PrettyManager.c;
    if (credential != null){
        credential.clear(context)
    }
    PrettyManager.c = null

    deleteCryptoFile(context, userName)

    Log.i("PrettyPassword", "Deleted user")
}