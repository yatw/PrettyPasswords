package com.prettypasswords.controller


import android.content.Context
import android.util.Log
import android.widget.Toast
import com.prettypasswords.PrettyManager
import com.prettypasswords.model.deleteCryptoFile
import com.prettypasswords.model.getFileWithUserName
import java.io.File
import java.util.*


fun getLastSessionUser(context: Context): String? {

    val sharedPref = context.getSharedPreferences(PrettyManager.sharedPreferenceKey, Context.MODE_PRIVATE)

    return sharedPref.getString("userName","")
}


// has credential doesn't mean login
fun hasCredential(context: Context, userName: String): Boolean{

    if (PrettyManager.c == null){

        val file: File? = getFileWithUserName(context, userName)
        restoreCredentialFromFile(file)
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

    credential.saveCurrentUser(context)

}

fun validatePassword(password: String): Boolean{

    // must have credential to validate
    val credential = PrettyManager.c!!

    val esk: ByteArray = credential.esk
    val pk: ByteArray = credential.pk

    val decryptedSK: ByteArray = PrettyManager.e.decryptESKtoSK(esk,  password)
    val generatedpk: ByteArray = PrettyManager.e.generatePk(decryptedSK)


    // if not equals, incorrect password
    if (!Arrays.equals(generatedpk,pk)){
        return false
    }

    return true
}

// use after user has credential
fun loginByPassword(context: Context, userName: String, password: String): Boolean{

    // must have credential to login
    val credential = PrettyManager.c!!

    val esk: ByteArray = credential.esk
    val pk: ByteArray = credential.pk

    val decryptedSK: ByteArray = PrettyManager.e.decryptESKtoSK(esk,  password)
    val generatedpk: ByteArray = PrettyManager.e.generatePk(decryptedSK)


    // if not equals, incorrect password
    if (!Arrays.equals(generatedpk,pk)){
        return false
    }

    credential.setSk(decryptedSK)
    credential.saveCurrentUser(context)
    PrettyManager.cm!!.body.decrypt(pk, decryptedSK)

    return true
}

fun changeUserName(context: Context, userName: String){

    val credential = PrettyManager.c!!

    val oldName = credential.userName

    credential.userName = userName
    PrettyManager.cm!!.updateContent()
    PrettyManager.cm!!.saveContentToDisk(context)

    credential.saveCurrentUser(context)

    // delete old file with old userName
    val file: File? = getFileWithUserName(context, oldName)
    deleteCryptoFile(context, file)

    Toast.makeText(context, "Change userName success", Toast.LENGTH_LONG).show()
}


fun changePassword(context: Context, password: String){

    val curCred = PrettyManager.c!!

    // same as create user, generate a new set of pk sk

    val userKeyPair : Array<ByteArray> = PrettyManager.e.generateAKey()

    val pk = userKeyPair[0]
    val sk = userKeyPair[1]

    val esk: ByteArray = PrettyManager.e.generateESKey(sk, password)

    val newCred = Credential(
        userName = curCred.userName,
        pk = pk,
        sk = sk,
        esk = esk
    )
    PrettyManager.c = newCred
    PrettyManager.cm!!.updateContent()
    PrettyManager.cm!!.saveContentToDisk(context)

    newCred.saveCurrentUser(context)

    Toast.makeText(context, "Change password success", Toast.LENGTH_LONG).show()
}

fun changeUserNameAndPassword(context: Context, userName: String, password: String){

    val curCred = PrettyManager.c!!
    val oldName = curCred.userName

    // same as create user, generate a new set of pk sk

    val userKeyPair : Array<ByteArray> = PrettyManager.e.generateAKey()

    val pk = userKeyPair[0]
    val sk = userKeyPair[1]

    val esk: ByteArray = PrettyManager.e.generateESKey(sk, password)

    val newCred = Credential(
        userName = userName,
        pk = pk,
        sk = sk,
        esk = esk
    )
    PrettyManager.c = newCred
    PrettyManager.cm!!.updateContent()
    PrettyManager.cm!!.saveContentToDisk(context)

    newCred.saveCurrentUser(context)

    // delete old file with old userName
    val file: File? = getFileWithUserName(context, oldName)
    deleteCryptoFile(context, file)

    Toast.makeText(context, "Change userName and password success", Toast.LENGTH_LONG).show()
}

fun logout(context: Context){


/*
    // This is normal logout, still have credential
    val credential = PrettyManager.c
    credential!!.setSk(null)
*/

    // this is hard logout, erase credential
    // but for this app, when signin hasContential() will restore
    PrettyManager.c = null
    PrettyManager.cm = null

}

fun deleteUser(context: Context, userName: String){


    val credential = PrettyManager.c
    if (credential != null){
        credential.clear(context)
    }
    PrettyManager.c = null

    deleteCryptoFile(context, userName)

    Log.i("PrettyPassword", "Deleted user")
}