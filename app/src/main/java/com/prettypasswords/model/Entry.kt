package com.prettypasswords.model


import android.content.Context
import com.prettypasswords.PrettyManager
import org.json.JSONObject
import java.io.Serializable
import java.util.*


class Entry {

    // properties
    var parentTag: Tag

    var name: String
    private var content: String
    var lastModified: String

    var isModified: Boolean = false

    constructor(parentTag: Tag, name:String){

        this.parentTag = parentTag
        this.name = name
        this.content = ""
        this.lastModified = "n/a"

    }


    constructor(parentTag: Tag, entry: JSONObject){

        this.parentTag = parentTag
        this.name = entry.getString("name")
        this.content = entry.getString("content")
        this.lastModified = entry.getString("lastModified")
    }

    fun getContent(): String{
        return content
    }

    fun save(context: Context, content: String){

        this.content = content

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



    fun build(): JSONObject{

        val build = JSONObject()
        build.put("name", name)
        build.put("content", content)
        build.put("lastModified", lastModified)
        return build
    }


}