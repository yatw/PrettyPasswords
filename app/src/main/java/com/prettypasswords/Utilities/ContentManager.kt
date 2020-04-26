package com.prettypasswords.Utilities

import android.content.Context
import android.util.Base64
import com.prettypasswords.PrettyManager
import com.prettypasswords.View.showAlert
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.HashMap


class ContentManager{

    // the whole file, credential and body
    private val content: JSONObject

    // if sectionBody is null, it is still encrypted as a b64 string
    // if sectionBody is not null, all tags stored inside are still encrypted
    private var sectionBody: JSONObject?

    // store decrypted tag for app section
    // if the value is null, that mean the content inside tag is encrypted
    private val sectionTags: HashMap<String, JSONObject?> = HashMap()

    constructor(){  // used when create user
        content = initializeContent()
        sectionBody = content.getJSONObject("body")
    }

    constructor(content: JSONObject){  // used when sign in, retrieve existing content
        this.content = content
        sectionBody = null
    }

    fun getContent(): JSONObject{
        return content
    }

    fun getBody(): JSONObject?{
        return sectionBody
    }

    fun getTags(): JSONArray{
        return sectionBody!!.getJSONArray("tags")
    }

    // When first create the cryptoFile, put in userName and esk
    private fun initializeContent(): JSONObject {

        val c = PrettyManager.c!!

        val content = JSONObject()

        // credential should contain userName, pk, esk
        val credential = JSONObject()
        credential.put("userName", c.userName)

        val b64pk = Base64.encodeToString(c.pk, Base64.DEFAULT)
        val b64esk = Base64.encodeToString(c.esk, Base64.DEFAULT)
        val b64xesak = Base64.encodeToString(c.xesak, Base64.DEFAULT)


        credential.put("b64esk", b64esk)
        credential.put("b64pk", b64pk)
        credential.put("b64xesak", b64xesak)


        content.put("credential", credential)


        // body should contain xesak, count, and entries
        val body = JSONObject()

        body.put("count", 0)
        body.put("tags", JSONArray())

        content.put("body", body)

        return content
    }



    // encrypt the whole body section of the file, value encrypted to string instead of JSONObject
    private fun encryptBody(): String{

        val e = PrettyManager.e
        val c: Credential = PrettyManager.c!!

        val sak = e.generateSAK(c.xesak, c.pk, c.getSk())
        //println("sak at encrypt ${sak.contentToString()}")

        val bbody = sectionBody.toString().toByteArray()
        //println("bbody at encrypt ${bbody.contentToString()}")

        val ebody = e.sKeyEncrypt(bbody, sak)
        //println("ebody at encrypt ${ebody.contentToString()}")

        val b64ebody = Base64.encodeToString(ebody, Base64.DEFAULT)
        //println("b64ebody at encrypt $b64ebody")

        return b64ebody
    }



    // will decrypt out all the tags name, but not the entries under the tag
    fun decryptBody(xesak: ByteArray, pk: ByteArray, sk: ByteArray){

        if (sectionBody == null){ // body is still encrypted

            val e = PrettyManager.e

            val sak = e.generateSAK(xesak, pk, sk)
            //println("sak at decrypt ${sak.contentToString()}")

            val b64ebody = content.getString("body")   // should be a decrypted b64 string, now turn it into JSON object
            //println("b64ebody at decrypt $b64ebody")

            val ebody = Base64.decode(b64ebody, Base64.DEFAULT)
            //println("ebody at decrypt ${ebody.contentToString()}")

            val bbody = e.sKeyDecrypt(ebody, sak)
            //println("bbody at decrypt ${bbody.contentToString()}")

            val sbody = bbody.toString(Charsets.UTF_8)
            //println("sbody at decrypt $sbody")

            sectionBody = JSONObject(sbody)

            // store all tags
            val tags: JSONArray = sectionBody!!.getJSONArray("tags")
            for (i in 0 until tags.length()) {
                val tag: JSONObject = tags.getJSONObject(i)
                sectionTags.put(tag.getString("tagName"),null)
            }

        }
    }



    fun addTag(context: Context, tagName: String, prettyPassword: String){


        if (sectionBody == null) {
            showAlert(context, "Body is encrypted", "cannot add a tag while body is encrypted")
            return
        }

        val tag = JSONObject()
        tag.put("tagName", tagName)
        tag.put("count", 0)
        tag.put("entries", JSONObject())

        val psk = PrettyManager.e.generateSKey()
        val hasedPP = PrettyManager.e.SHA256(prettyPassword.toByteArray())
        val epsk = PrettyManager.e.sKeyEncrypt(psk, hasedPP)
        val b64epsk = Base64.encodeToString(epsk, Base64.DEFAULT)
        tag.put("b64epsk", b64epsk)

        val cc: Calendar = Calendar.getInstance()
        val year: Int = cc.get(Calendar.YEAR)
        val month: Int = cc.get(Calendar.MONTH)+ 1    // only month start from 0, weird
        val day: Int = cc.get(Calendar.DAY_OF_MONTH)
        tag.put("lastModified", "$month/$day/$year")

        sectionTags.put(tagName, tag)
        sectionBody!!.put("count", sectionBody!!.getInt("count")+1)


        // encrypt this newly created tag and put into sectionBody
        val encryptedTag = JSONObject(tag.toString())
        encryptedTag.put("entries", encryptEntries(tag.getJSONObject("entries"), psk))
        val tags = sectionBody!!.getJSONArray("tags")
        tags.put(tag)

        saveContentToDisk(context)

    }


    private fun encryptEntries(entries: JSONObject, psk: ByteArray): String{

        val bentries = entries.toString().toByteArray()

        val eentries = PrettyManager.e.sKeyEncrypt(bentries, psk)

        val b64eentries = Base64.encodeToString(eentries, Base64.DEFAULT)

        return b64eentries
    }

    fun decryptEntries(tagName: String, prettyPassword: String){


        if (sectionTags.get(tagName) == null) {

            val tags = sectionBody!!.getJSONObject("tags")
            val tag = tags.getJSONObject(tagName)

            val decryptedTag = JSONObject(tag.toString())
            sectionTags.put(tagName, decryptedTag)

            val b64eentries = tag.getString("entries")
            val eentries = Base64.decode(b64eentries, Base64.DEFAULT)

            val b64epsk = tag.getString("b64epsk")
            val epsk = Base64.decode(b64epsk, Base64.DEFAULT)

            val hasedPP = PrettyManager.e.SHA256(prettyPassword.toByteArray())
            val psk = PrettyManager.e.sKeyDecrypt(epsk, hasedPP)

            val bentries = PrettyManager.e.sKeyDecrypt(eentries, psk)

            val sentries = bentries.toString(Charsets.UTF_8)

            val entries = JSONObject(sentries)

            decryptedTag.put("entries", entries)

        }
    }


    fun saveContentToDisk(context: Context){
        if (sectionBody != null){
            content.put("body", encryptBody())
        }
        createCryptoFile(context, content)
    }


}

