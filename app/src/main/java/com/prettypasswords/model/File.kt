package com.prettypasswords.model

import android.content.Context
import com.prettypasswords.PrettyManager
import com.prettypasswords.R
import com.prettypasswords.controller.restoreCredential
import com.prettypasswords.view.popups.showAlert
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.lang.Exception
import java.nio.charset.StandardCharsets


// file operation related
// https://developer.android.com/training/data-storage/app-specific#kotlin

fun createCryptoFile(context: Context, eContent: JSONObject){

    val credential = PrettyManager.c!!

    //println("eContenet at save file $eContent")


    val fileName = context.getResources().getString(R.string.cryptoFilePrefix) + credential.userName
    val file = File(context.filesDir, fileName )

    try {

        if (file.exists()){
            deleteCryptoFile(context, fileName)
        }

        file.createNewFile()

        // write content into file
        context.openFileOutput(fileName, Context.MODE_PRIVATE).use {
            it.write(eContent.toString().toByteArray())

        }

    } catch (e: IOException) {
        showAlert(
            context,
            "Create Crypto failed",
            e.toString()
        )
        e.printStackTrace()
    }


}

fun importFile(context: Context, file: File): Boolean{


    if (!file.exists()){
        showAlert(
            context,
            "File do not exist",
            ""
        )
        return false
    }

    val fileContent: JSONObject? = getFileContent(file)

    if (fileContent == null){
        showAlert(
            context,
            "Get fileContent failed",
            "Probably not in json format"
        )
        return false
    }


    if (!validateFile(fileContent)){
        showAlert(
            context,
            "ValidateFile failed",
            ""
        )
        return false
    }

    restoreCredential(fileContent)

    createCryptoFile(context, fileContent)

    showAlert(
        context,
        "Import File Success",
        "You can now access this account"
    )
    return true

}

// if the file content has all these fields, then I think it is valid
fun validateFile(jsonContent: JSONObject): Boolean{

    try {

        val credential = jsonContent.getJSONObject("credential")
        val body = jsonContent.getJSONObject("body")

        val userName = credential.getString("userName")
        val b64pk = credential.getString("b64pk")
        val b64esk = credential.getString("b64esk")

        val b64xesak = body.getString("b64xesak")
        val b64etags = body.getString("b64etags")

        return true

    }catch(e: JSONException){
        e.printStackTrace()
    }

    return false
}

// turn the file content into JSONObject
fun getFileContent(file: File): JSONObject?{

    val fis = FileInputStream(file)
    val inputStreamReader = InputStreamReader(fis, StandardCharsets.UTF_8)
    val stringBuilder = StringBuilder()

    val reader = BufferedReader(inputStreamReader)

    var line: String? = reader.readLine()
    while (line != null) {
        stringBuilder.append(line).append('\n')
        line = reader.readLine()
    }

    val contents = stringBuilder.toString()

    return try{
        JSONObject(contents);

    }catch (e: JSONException){
        null
    }

}

fun getFileWithUserName(context: Context, userName: String): File?{

    val predicatedName = context.resources.getString(R.string.cryptoFilePrefix) + userName

    val files: Array<String> = context.fileList()   // all files within the filesDir directory

    for (filename in files){

        if (filename == predicatedName){
            return File(context.filesDir, filename)
        }
    }

    return null
}

fun deleteCryptoFile(context: Context, fileName: String){


    val file = File(context.filesDir, fileName )

    val status = file.delete()
    //println("File deleted: $fileName status is $status")

}

fun deleteCryptoFile(file: File?){

    if (file != null){
        val status = file.delete()
        //println("File deleted: ${file.name} status is $status")
    }

}