package com.prettypasswords.controller

import android.content.Context
import android.util.Base64
import com.prettypasswords.PrettyManager
import org.json.JSONObject


fun restoreCredential(fileContent: JSONObject){

    val fileCredential = fileContent.getJSONObject("credential")


    val userName = fileCredential.getString("userName")
    val b64esk = fileCredential.getString("b64esk")
    val b64pk = fileCredential.getString("b64pk")

    val pk: ByteArray = Base64.decode(b64pk, Base64.DEFAULT)
    val esk: ByteArray = Base64.decode(b64esk, Base64.DEFAULT)

    PrettyManager.c = Credential(userName = userName, pk = pk, esk = esk)
    PrettyManager.cm = ContentManager(fileContent)

}



class Credential(
    var userName: String,
    val pk: ByteArray,
    private var sk: ByteArray? = null,
    val esk: ByteArray
) {

    fun getSk(): ByteArray?{
        return sk
    }

    fun setSk(sk: ByteArray?){
        this.sk = sk
    }

    fun clear(context: Context) {

        val sharedPref = context.getSharedPreferences(PrettyManager.sharedPreferenceKey, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.clear()
        editor.commit()
        sk = null
    }

}