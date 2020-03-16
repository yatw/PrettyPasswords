package com.prettypasswords.Utilities


import android.content.Context
import android.util.Log
import com.prettypasswords.PrettyManager
import java.util.*

// register, to create cryptoFile
fun createUser(context: Context, userName: String, password: String){

    val userKeyPair : Array<ByteArray> = PrettyManager.e.generateAKey()

    val pk = userKeyPair[0]
    val sk = userKeyPair[1]

    val esk: ByteArray = PrettyManager.e.generateESKey(sk, password)

    PrettyManager.c = Credential(userName=userName,pk=pk,sk=sk,esk=esk)


    // create crypto file
    //https://developer.android.com/training/data-storage/app-specific#java
    createCryptoFile(context)

    Log.i("PrettyPassword", "Created user")

}

// use after user has credential
fun loginByPassword(context: Context, userName: String, password: String): Boolean{

    // first see if there is credential from retreiveCredential
    var credential: Credential? = PrettyManager.c

    // Even after retrieveCredential and saved credential
    // credential class can be null after exit the app
    // so try to restore credential from disk
    if (credential == null){

        credential = restoreCredential(context,userName)

        if (credential == null){
            // Incorrect flow, should never have empty credential when calling this function
            // new device should always retrieve credential before loginbypassword
            throw AssertionError("New Device: No credential found")
        }
    }

    val esk: ByteArray = credential.esk
    val pk: ByteArray = credential.pk

    val decryptedSK: ByteArray = PrettyManager.e.decryptESKtoSK(esk,  password)
    val generatedpk: ByteArray = PrettyManager.e.generatePk(decryptedSK)


    // if not equals, incorrect password
    if (!Arrays.equals(generatedpk,pk)){
        return false
    }

    credential.setSk(decryptedSK)
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