package com.prettypasswords.model

import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class Password(
    var siteName: String,
    var userName: String,
    var email: String,
    var password: String,
    var others: String?,
    var lastModified: String = ""
){

    init {
        if (lastModified.isBlank()){
            lastModified = getDate()
        }
    }

    fun toJson(): JSONObject{
        return JSONObject().apply {
            this.put("siteName", siteName)
            this.put("userName", userName)
            this.put("email", email)
            this.put("password", password)

            if (others != null){
                this.put("others", others)
            }
            this.put("lastModified", lastModified)
        }
    }

    private fun getDate(): String {
        val c: Date = Calendar.getInstance().getTime()
        val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd")
        return simpleDateFormat.format(c)
    }

    override fun toString(): String{
        return """
            Password(siteName=$siteName, userName=$userName, email=$email, password=$password, others=$others, lastModified=$lastModified
        """
    }
}
