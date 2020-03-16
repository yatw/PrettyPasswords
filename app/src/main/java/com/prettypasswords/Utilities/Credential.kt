package com.prettypasswords.Utilities

import android.content.Context
import android.util.Base64
import com.prettypasswords.PrettyManager
import org.json.JSONObject


// try to create a credential from sharedpreference
// or
// if sharedpreference don't have, restore from cryptoFile
fun restoreCredential(context: Context, userName: String): Credential?{


    val sharedPref = context.getSharedPreferences(PrettyManager.sharedPreferenceKey, Context.MODE_PRIVATE)

    val b64pk = sharedPref.getString("pk$userName", "")
    val b64esk = sharedPref.getString("esk$userName", "")

    // if missing credential from disk
    // try restore from cryptoFile
    if (b64pk.equals("") || b64esk.equals("")){
        return restoreCredentialFromFile(context, userName)
    }

    val pk: ByteArray = Base64.decode(b64pk, Base64.DEFAULT)
    val esk: ByteArray = Base64.decode(b64esk, Base64.DEFAULT)

    return Credential(userName = userName, pk = pk, esk = esk)

}

fun restoreCredentialFromFile(context: Context, userName: String): Credential{

    val fileContent: JSONObject = getFileContent(context, userName)

    val b64esk = fileContent.getString("b64esk");
    val b64pk = fileContent.getString("b64pk");

    val pk: ByteArray = Base64.decode(b64pk, Base64.DEFAULT)
    val esk: ByteArray = Base64.decode(b64esk, Base64.DEFAULT)

    return Credential(userName = userName, pk = pk, esk = esk)

}

class Credential(
    val userName: String,
    val pk: ByteArray,
    private var sk: ByteArray? = null,
    val esk: ByteArray
) {

    fun getSk(): ByteArray?{
        return sk
    }

    fun setSk(sk: ByteArray){
        this.sk = sk
    }


    fun save(context: Context){

        val sharedPref = context.getSharedPreferences(PrettyManager.sharedPreferenceKey, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        if (pk != null){
            val b64pk = Base64.encodeToString(pk, Base64.DEFAULT)
            editor.putString("pk$userName", b64pk)
        }

        if (esk != null){
            val b64esk  = Base64.encodeToString(esk, Base64.DEFAULT)
            editor.putString("esk$userName", b64esk )
        }

        editor.commit()
    }


    fun clear(context: Context) {

        val sharedPref = context.getSharedPreferences(PrettyManager.sharedPreferenceKey, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.clear()
        editor.commit()
        sk = null
    }

}