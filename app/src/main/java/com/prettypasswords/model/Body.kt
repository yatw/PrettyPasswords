package com.prettypasswords.model

import android.content.Context
import android.content.Intent
import android.util.Base64
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.prettypasswords.PrettyManager
import com.prettypasswords.controller.Credential
import com.prettypasswords.view.activities.EntriesListActivity
import org.json.JSONArray
import org.json.JSONObject
import kotlin.collections.ArrayList

class Body {

    // properties
    private var b64xesak: String
    private var b64etags: String?  // updated when save to disk

    private var sak: ByteArray? = null
    val tags: ArrayList<Tag> = ArrayList()

    // used when create user, there is no content
    constructor(){

        // create a xesak, and save it as b64 string
        val c: Credential = PrettyManager.c!!
        val xesak = PrettyManager.e.generateXESAK(c.pk, c.getSk())
        b64xesak = Base64.encodeToString(xesak, Base64.DEFAULT)

        // decrypt sak from xesak
        sak = PrettyManager.e.generateSAK(xesak, c.pk, c.getSk())

        b64etags = null  // let updateTags encrypt to string when saveToDisk
    }


    constructor(body: JSONObject){
        b64xesak = body.getString("b64xesak")
        b64etags = body.getString("b64etags")
    }


    // use by updateTags()
    private fun encrypt(btags: ByteArray): String{

        val etags = PrettyManager.e.sKeyEncrypt(btags, sak)

        val b64etags = Base64.encodeToString(etags, Base64.DEFAULT)

        return b64etags
    }


    // called when user login success
    // if login success, the sak will be valid
    fun decrypt(pk: ByteArray, sk: ByteArray){

        val e = PrettyManager.e

        val xesak = Base64.decode(b64xesak, Base64.DEFAULT)

        sak = e.generateSAK(xesak, pk, sk)

        val etags = Base64.decode(b64etags, Base64.DEFAULT)

        val btags = e.sKeyDecrypt(etags, sak)

        val stags = btags.toString(Charsets.UTF_8)


        val tagsJSON = JSONArray(stags)

        // store all tags
        for (i in 0 until tagsJSON.length()) {
            val tag = Tag(tagsJSON.getJSONObject(i))
            tags.add(tag)
        }

    }

    // when user change password, everything need to be re=encrypted
    fun reencrypt(){

        // create a xesak, and save it as b64 string
        val c: Credential = PrettyManager.c!!
        val xesak = PrettyManager.e.generateXESAK(c.pk, c.getSk())
        b64xesak = Base64.encodeToString(xesak, Base64.DEFAULT)

        // decrypt sak from xesak
        sak = PrettyManager.e.generateSAK(xesak, c.pk, c.getSk())

        updateTags()

    }


    fun addTag(context: Context, tagName: String, prettyPassword: String){

        val newTag = Tag(tagName, prettyPassword)
        tags.add(newTag)
        PrettyManager.cm!!.saveContentToDisk(context)


        val intent = Intent("addTagSuccess")
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
    }

    fun deleteTag(context: Context, tag: Tag){

        tags.remove(tag)
        PrettyManager.cm!!.saveContentToDisk(context)

        Toast.makeText(context, "Tag ${tag.tagName} deleted", Toast.LENGTH_LONG).show()

    }

    private fun updateTags(){

        val updatedTags = JSONArray()

        for (i in 0 until tags.size) {
            val tag: Tag = tags.get(i)
            updatedTags.put(tag.build())
        }

        // now encrypt
        val btags = updatedTags.toString().toByteArray()
        b64etags = encrypt(btags)
    }

    fun build(): JSONObject{

        updateTags()

        val build = JSONObject()
        build.put("b64xesak", b64xesak)
        build.put("b64etags", b64etags)
        return build

    }


}