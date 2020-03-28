package com.prettypasswords.Utilities

import android.content.Context
import android.util.Base64
import com.prettypasswords.PrettyManager
import org.json.JSONObject


fun restoreCredentialFromFile(context: Context, userName: String): Credential?{

    val fileContent: JSONObject? = getFileContent(context, userName)

    if (fileContent != null){

        val fileCredential = fileContent.getJSONObject("credential")

        val b64esk = fileCredential.getString("b64esk")
        val b64pk = fileCredential.getString("b64pk")
        val b64xesak = fileCredential.getString("b64xesak")

        val pk: ByteArray = Base64.decode(b64pk, Base64.DEFAULT)
        val esk: ByteArray = Base64.decode(b64esk, Base64.DEFAULT)
        val xesak: ByteArray = Base64.decode(b64xesak, Base64.DEFAULT)

        val credential = Credential(userName = userName, pk = pk, esk = esk, xesak=xesak)
        PrettyManager.c = credential
        PrettyManager.cm = ContentManager(fileContent)
    }


    return null
}

class Credential(
    val userName: String,
    val pk: ByteArray,
    private var sk: ByteArray? = null,
    val esk: ByteArray,
    val xesak: ByteArray
) {

    fun getSk(): ByteArray?{
        return sk
    }

    fun setSk(sk: ByteArray){
        this.sk = sk
    }



    // save the userName in sharedpreference on create and on login
    fun saveUserName(context: Context){

        val sharedPref = context.getSharedPreferences(PrettyManager.sharedPreferenceKey, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        editor.putString("userName", userName)

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