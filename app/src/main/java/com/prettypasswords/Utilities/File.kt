package com.prettypasswords.Utilities

import android.content.Context
import android.util.Base64
import com.prettypasswords.PrettyManager
import com.prettypasswords.R
import org.json.JSONObject
import java.io.*
import java.nio.charset.StandardCharsets


// file operation related
// https://developer.android.com/training/data-storage/app-specific#kotlin

fun createCryptoFile(context: Context){

    val credential = PrettyManager.c!!

    val content = initializeContent(credential)

    val eContent = encryptBody(content)


    val fileName = context.getResources().getString(R.string.cryptoFilePrefix) + credential.userName;
    val file = File(context.filesDir, fileName )

    try {
        if (file.createNewFile()) {
            println("File created: " + file.name)

            // write content into file
            context.openFileOutput(fileName, Context.MODE_PRIVATE).use {
                it.write(eContent.toString().toByteArray())
            }

            PrettyManager.cm = ContentManager(eContent)

        } else {
            println("File already exists.")
        }
    } catch (e: IOException) {
        println("An error occurred.")
        e.printStackTrace()
    }

}



// When first create the cryptoFile, put in userName and esk
fun initializeContent(c: Credential): JSONObject{

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
    body.put("tags", JSONObject())

    content.put("body", body)

    return content
}


fun deleteCryptoFile(context: Context, userName: String){


    val fileName = context.getResources().getString(R.string.cryptoFilePrefix) + userName;
    val file = File(context.filesDir, fileName )

    file.delete()
}


fun getFileContent(context: Context, userName: String): JSONObject?{

    val fileName = context.resources.getString(R.string.cryptoFilePrefix) + userName;

    try {

        val fis: FileInputStream = context.openFileInput(fileName)
        val inputStreamReader = InputStreamReader(fis, StandardCharsets.UTF_8)
        val stringBuilder = StringBuilder()

        val reader = BufferedReader(inputStreamReader)

        var line: String? = reader.readLine()
        while (line != null) {
            stringBuilder.append(line).append('\n')
            line = reader.readLine()
        }

        val contents = stringBuilder.toString()

        return JSONObject(contents);

    } catch (e1: FileNotFoundException) {
       // throw AssertionError("No Crypto File") // TODO alert instead

        println("No Crypto file")

    } catch (e2: IOException){
        //throw AssertionError("Have Crypto File but Error open file")  //  TODO alert instead
        println("Have Crypto File but Error open file")
    }

    return null

}