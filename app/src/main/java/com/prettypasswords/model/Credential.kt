package com.prettypasswords.model

import android.content.Context
import android.util.Base64
import com.prettypasswords.globals.PrettyManager
import org.json.JSONObject

class Credential(
    var userName: String,
    val pk: ByteArray,
    private var sk: ByteArray? = null,
    val esk: ByteArray,
    val xesak: ByteArray
) {


    private var sak: ByteArray? = null

    fun hasSk(): Boolean{
        return sk != null
    }

    fun getSk(): ByteArray?{
        return sk
    }

    fun setSk(sk: ByteArray?){
        this.sk = sk
    }

    fun getSak(): ByteArray{
        if (sk == null){
            throw IllegalStateException("sk is null")
        }
        return sak ?: synchronized(this) {
            val sak = PrettyManager.e.generateSAK(xesak, pk, sk)
            this.sak = sak
            sak
        }
    }

    fun clear(context: Context) {

        val sharedPref = context.getSharedPreferences(PrettyManager.sharedPreferenceKey, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.clear()
        editor.commit()
        sk = null
    }

    fun toJson(): JSONObject{
        // credential should contain userName, pk, esk
        val credential = JSONObject()

        credential.put("userName", userName)

        val b64pk = Base64.encodeToString(pk, Base64.DEFAULT)
        val b64esk = Base64.encodeToString(esk, Base64.DEFAULT)
        val b64xesak = Base64.encodeToString(xesak, Base64.DEFAULT)
        credential.put("b64pk", b64pk)
        credential.put("b64esk", b64esk)
        credential.put("b64xesak", b64xesak)

        return credential
    }

}