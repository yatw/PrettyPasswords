package com.prettypasswords.Utilities

import android.content.Context
import android.util.Base64
import com.prettypasswords.PrettyManager
import com.prettypasswords.R
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import java.nio.charset.StandardCharsets


// file operation related
// https://developer.android.com/training/data-storage/app-specific#kotlin

fun createCryptoFile(context: Context){

    val credential = PrettyManager.c

    if (credential == null){
        throw AssertionError("No userCredential at createCryptoFile")
    }

    val content = initializeContent(credential)


    val fileName = context.getResources().getString(R.string.cryptoFilePrefix) + credential.userName;
    val file = File(context.filesDir, fileName )

    try {
        if (file.createNewFile()) {
            println("File created: " + file.name)

            // write content into file
            context.openFileOutput(fileName, Context.MODE_PRIVATE).use {
                it.write(content.toByteArray())
            }

        } else {
            println("File already exists.")
        }
    } catch (e: IOException) {
        println("An error occurred.")
        e.printStackTrace()
    }

}

// When first create the cryptoFile, put in userName and esk
fun initializeContent(c: Credential): String{

    val content = JSONObject()

    // credential should contain userName, pk, esk
    val credential = JSONObject()
    credential.put("userName", c.userName)

    val b64pk = Base64.encodeToString(c.pk, Base64.DEFAULT)
    val b64esk = Base64.encodeToString(c.esk, Base64.DEFAULT)

    println("b64pk $b64pk")
    println("b64esk $b64esk")

    credential.put("b64esk", b64esk)
    credential.put("b64pk", b64pk)

    content.put("credential", credential)


    // body should contain xesak, count, and entries
    val body = JSONObject()

    val xesak = PrettyManager.e.generateXESAK(c.pk, c.getSk())
    val b64xesak = Base64.encodeToString(xesak, Base64.DEFAULT)
    body.put("b64xesak", b64xesak)
    body.put("count", 0)
    body.put("entries", JSONArray())

    content.put("body", body)

    return content.toString()
}


fun deleteCryptoFile(context: Context, userName: String){


    val fileName = context.getResources().getString(R.string.cryptoFilePrefix) + userName;
    val file = File(context.filesDir, fileName )

    file.delete()
}


fun getFileContent(context: Context, userName: String): JSONObject{

    val fileName = context.resources.getString(R.string.cryptoFilePrefix) + userName;

    val fis: FileInputStream = context.openFileInput(fileName)
    val inputStreamReader = InputStreamReader(fis, StandardCharsets.UTF_8)
    val stringBuilder = StringBuilder()

    try {

        val reader = BufferedReader(inputStreamReader)

        var line: String? = reader.readLine()
        while (line != null) {
            stringBuilder.append(line).append('\n')
            line = reader.readLine()
        }

    } catch (e: IOException) { // Error occurred when opening raw file for reading.

        throw AssertionError("Error open file")
    }

    val contents = stringBuilder.toString()

    return JSONObject(contents);

}