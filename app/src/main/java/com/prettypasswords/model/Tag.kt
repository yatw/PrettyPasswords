package com.prettypasswords.model

import android.content.Context
import android.content.Intent
import android.util.Base64
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.prettypasswords.PrettyManager
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class Tag {

    // properties
    val tagName: String
    var lastModified: String
    private val b64epsk: String
    private var b64eentries: String?  // base64 Encrypted entries


    private var psk: ByteArray?
    val entries: ArrayList<Entry> = ArrayList()

    // used when create a new tag
    constructor(tagName: String, prettyPassword: String){

        this.tagName = tagName

        psk = PrettyManager.e.generateSKey()
        val hasedPP = PrettyManager.e.SHA256(prettyPassword.toByteArray())
        val epsk = PrettyManager.e.sKeyEncrypt(psk, hasedPP)
        b64epsk = Base64.encodeToString(epsk, Base64.DEFAULT)


        val cc: Calendar = Calendar.getInstance()
        val year: Int = cc.get(Calendar.YEAR)
        val month: Int = cc.get(Calendar.MONTH)+ 1    // only month start from 0, weird
        val day: Int = cc.get(Calendar.DAY_OF_MONTH)
        lastModified = "$month/$day/$year"

        b64eentries = null // let updateEntries encrypt to string when saveToDisk
    }

    constructor(encryptedTag: JSONObject){

        this.tagName = encryptedTag.getString("tagName")
        this.b64epsk = encryptedTag.getString("b64epsk")
        this.b64eentries = encryptedTag.getString("b64eentries")
        this.lastModified = encryptedTag.getString("lastModified")
        this.psk = null
    }

    fun decrypted(): Boolean{
        return psk != null
    }


    fun decrypt(prettyPassword: String): Boolean{

        val eentries = Base64.decode(b64eentries, Base64.DEFAULT)

        val epsk = Base64.decode(b64epsk, Base64.DEFAULT)

        val hasedPP = PrettyManager.e.SHA256(prettyPassword.toByteArray())
        val psk = PrettyManager.e.sKeyDecrypt(epsk, hasedPP)

        val bentries = PrettyManager.e.sKeyDecrypt(eentries, psk)

        val sentries = bentries.toString(Charsets.UTF_8)


        // if tag password is correct, sentries will be able to restore back to a json object
        try{

            val entriesJSON = JSONArray(sentries)


            println("decrypted json" + entriesJSON)

            for (i in 0 until entriesJSON.length()){
                entries.add(Entry(this, entriesJSON.getJSONObject(i)))
            }

            this.psk = psk
            return true

        }catch (e: JSONException){
            e.printStackTrace()
            return false
        }

    }


    fun addEntry(context: Context, name: String){

        val newEntry = Entry(this, name)
        entries.add(newEntry)
        PrettyManager.cm!!.saveContentToDisk(context)

        Toast.makeText(context, "Entry $name created", Toast.LENGTH_LONG).show()

        val intent = Intent("addEntrySuccess")
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
    }


    private fun updateEntries(){

        // if this tag has not been decrypted, the b64eentries will not change
        // if this tag has been decrypted, the entries inside might have been updated
        if (psk != null) {

            val updatedEntries = JSONArray()

            for (i in 0 until entries.size) {
                val entry: Entry = entries.get(i)
                println("get content at tag " + entry.getContent())
                updatedEntries.put(entry.build())

                if (entry.isModified){
                    this.lastModified = entry.lastModified
                }
            }

            // now encrypt
            val bentries = updatedEntries.toString().toByteArray()
            val eentries = PrettyManager.e.sKeyEncrypt(bentries, psk!!)
            b64eentries = Base64.encodeToString(eentries, Base64.DEFAULT)
        }
    }

    fun build(): JSONObject{

        updateEntries()

        val build = JSONObject()
        build.put("b64epsk", b64epsk)
        build.put("tagName", tagName)
        build.put("b64eentries", b64eentries)
        build.put("lastModified", lastModified)
        return build
    }

}