package com.prettypasswords.model


import android.content.Context
import com.prettypasswords.PrettyManager
import org.json.JSONObject
import java.util.*


class Entry {

    // properties
    var name: String
    private val content: JSONObject
    var lastModified: String

    var isModified: Boolean = false

    constructor(name:String){

        this.name = name
        this.content = JSONObject()
        this.lastModified = "n/a"

    }


    constructor(entry: JSONObject){

        this.name = entry.getString("name")
        this.content = entry.getJSONObject("content")
        this.lastModified = entry.getString("lastModified")
    }

    private fun add(context: Context, key: String, value: String){
        content.put(key, value)

    }

    private fun remove(context: Context, key: String){
        content.remove(key)
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