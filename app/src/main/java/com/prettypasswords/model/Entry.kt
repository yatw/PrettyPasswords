package com.prettypasswords.model


import android.content.Context
import com.prettypasswords.PrettyManager
import org.json.JSONObject
import java.util.*


class Entry {

    // properties
    var parentTag: Tag

    var siteName: String
    var userName: String
    var email: String
    var password: String
    var others: String
    var lastModified: String

    var isModified: Boolean = false

    constructor(parentTag: Tag, siteName:String, userName: String, password: String, email: String, others: String){

        this.parentTag = parentTag

        this.siteName = siteName
        this.userName = userName
        this.password = password
        this.email = email
        this.others = others

        this.lastModified = "n/a"

    }


    constructor(parentTag: Tag, entry: JSONObject){

        this.parentTag = parentTag

        this.siteName = entry.getString("siteName")
        this.userName = entry.getString("userName")
        this.email = entry.getString("email")
        this.password = entry.getString("password")
        this.others = entry.getString("others")
        this.lastModified = entry.getString("lastModified")
    }


    fun save(context: Context, siteName:String, userName: String, password: String, email: String, others: String ){

        this.siteName = siteName
        this.userName = userName
        this.password = password
        this.email = email
        this.others = others
        updateLastModified(context)
    }

    private fun updateLastModified(context: Context){

        val cc: Calendar = Calendar.getInstance()
        val year: Int = cc.get(Calendar.YEAR)
        val month: Int = cc.get(Calendar.MONTH)+ 1    // only month start from 0, weird
        val day: Int = cc.get(Calendar.DAY_OF_MONTH)

        lastModified = "$month/$day/$year"
        isModified = true

        PrettyManager.cm!!.saveContentToDisk(context)
    }

    fun delete(context: Context){

        this.parentTag.deleteEntry(context, this)
    }



    fun build(): JSONObject{

        val build = JSONObject()
        build.put("siteName", siteName)
        build.put("userName", userName)
        build.put("email", email)
        build.put("password", password)
        build.put("others", others)
        build.put("lastModified", lastModified)

        return build
    }


}