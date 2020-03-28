package com.prettypasswords.Utilities

import android.content.Context
import android.util.Base64
import com.prettypasswords.PrettyManager
import org.json.JSONObject

// encrypt the whole body section of the file, value encrypted to string instead of JSONObject
fun encryptBody(content:JSONObject): JSONObject{

    val e = PrettyManager.e
    val c: Credential = PrettyManager.c!!

    val sak = e.generateSAK(c.xesak, c.pk, c.getSk())

    val body = content.getJSONObject("body")
    val bbody = body.toString().toByteArray()
    val ebody = e.sKeyEncrypt(bbody, sak)
    val b64ebody = Base64.encodeToString(ebody, Base64.DEFAULT)

    content.put("body", b64ebody)

    return content
}



class ContentManager(
    val eContent: JSONObject // the actual content that will store into file, always remain encrypted
) {

    val content: JSONObject = eContent   // the temporary content decrypted from user input used during app session


    // will shows all the tags name, but not the entries under the tag
    fun decryptBody(){

        val e = PrettyManager.e
        val c = PrettyManager.c!!

        val sak = e.generateSAK(c.xesak, c.pk, c.getSk())

        val b64ebody = eContent.getString("body")   // should be a decrypted b64 string, now turn it into JSON object
        val ebody = Base64.decode(b64ebody, Base64.DEFAULT)
        val bbody = e.sKeyDecrypt(ebody, sak)
        val sbody = bbody.toString(Charsets.UTF_8)

        val body = JSONObject(sbody)

        content.put("body", body)
    }


    fun addTag(context: Context, tagName: String, prettyPassword: String){

        val e = PrettyManager.e;

        val psk = e.generateSKey()

        val hashedPw = prettyPassword.toByteArray()

        val epsk = e.sKeyEncrypt(psk, hashedPw)

    }


}

