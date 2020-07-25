package com.prettypasswords.controller

import android.content.Context
import android.util.Base64
import com.prettypasswords.model.Body
import com.prettypasswords.PrettyManager
import com.prettypasswords.model.createCryptoFile
import org.json.JSONObject


class ContentManager{


    private var credential: JSONObject
    var body: Body


    constructor(){  // used when create user

        credential = initCredential()
        body = Body()
    }

    constructor(content: JSONObject){  // used when retrieve credential, before sign in

        credential = content.getJSONObject("credential")
        body = Body(content.getJSONObject("body"))
    }


    // generate JSONObject credential from object credential
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

    // when user change password, everything has to be re-encrypted
    fun updateContent(){

        credential = initCredential()
        body.reencrypt()
    }


    fun saveContentToDisk(context: Context){
        val eContent = getEncryptedContent()
        createCryptoFile(context, eContent)
    }

    fun getEncryptedContent(): JSONObject{

        val content = JSONObject()
        content.put("credential", credential)
        content.put("body", body.build())
        return content
    }


}

