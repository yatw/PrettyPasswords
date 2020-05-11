package com.prettypasswords.controller

import android.content.Context
import android.util.Base64
import com.prettypasswords.model.Body
import com.prettypasswords.PrettyManager
import com.prettypasswords.model.createCryptoFile
import org.json.JSONObject


class ContentManager{


    private val credential: JSONObject
    var body: Body


    constructor(){  // used when create user

        credential = initCredential()
        body = Body()
    }

    constructor(content: JSONObject){  // used when retrieve credential, before sign in

        credential = content.getJSONObject("credential")
        body = Body(content.getJSONObject("body"))
    }


    // When first create the cryptoFile, put in userName and esk
    private fun initCredential(): JSONObject {

        val c = PrettyManager.c!!

        // credential should contain userName, pk, esk
        val credential = JSONObject()

        credential.put("userName", c.userName)

        val b64pk = Base64.encodeToString(c.pk, Base64.DEFAULT)
        val b64esk = Base64.encodeToString(c.esk, Base64.DEFAULT)
        credential.put("b64pk", b64pk)
        credential.put("b64esk", b64esk)


        return credential
    }


    fun saveContentToDisk(context: Context){

        val content = JSONObject()
        content.put("credential", credential)
        content.put("body", body.build())
        createCryptoFile(context, content)
    }


}

