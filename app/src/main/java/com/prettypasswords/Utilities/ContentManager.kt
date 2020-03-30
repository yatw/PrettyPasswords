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
    //println("sak at encrypt ${sak.contentToString()}")

    val body = content.getJSONObject("body")
    val bbody = body.toString().toByteArray()
    //println("bbody at encrypt ${bbody.contentToString()}")

    val ebody = e.sKeyEncrypt(bbody, sak)
    //println("ebody at encrypt ${ebody.contentToString()}")


    val decryptedbbody = e.sKeyDecrypt(ebody, sak)
    //println("decryptedbbody at encrypt ${decryptedbbody.contentToString()}")


    val b64ebody = Base64.encodeToString(ebody, Base64.DEFAULT)
    //println("b64ebody at encrypt $b64ebody")

    content.put("body", b64ebody)

    return content
}



class ContentManager(
    val fileContent: JSONObject // the copy that record any file edition and will save into disk, always encrypted
) {
    val tempContent: JSONObject = fileContent // the temporary copy for user to read during app session, you see what you decrypted
    var bodyEncrypted: Boolean = true


    // will shows all the tags name, but not the entries under the tag
    fun decryptBody(){


        if (bodyEncrypted){

            val e = PrettyManager.e
            val c = PrettyManager.c!!

            val sak = e.generateSAK(c.xesak, c.pk, c.getSk())
            //println("sak at decrypt ${sak.contentToString()}")

            val b64ebody = tempContent.getString("body")   // should be a decrypted b64 string, now turn it into JSON object
            //println("b64ebody at decrypt $b64ebody")

            val ebody = Base64.decode(b64ebody, Base64.DEFAULT)
            //println("ebody at decrypt ${ebody.contentToString()}")

            val bbody = e.sKeyDecrypt(ebody, sak)
            //println("bbody at decrypt ${bbody.contentToString()}")

            val sbody = bbody.toString(Charsets.UTF_8)
            //println("sbody at decrypt $sbody")

            val body = JSONObject(sbody)

            tempContent.put("body", body)
            bodyEncrypted = false
        }


    }


    fun addTag(context: Context, tagName: String, prettyPassword: String){

        val e = PrettyManager.e;

        val psk = e.generateSKey()

        val hashedPw = prettyPassword.toByteArray()

        val epsk = e.sKeyEncrypt(psk, hashedPw)

    }


}

