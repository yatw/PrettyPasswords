package com.prettypasswords.model

import android.content.Context
import com.prettypasswords.PrettyManager
import com.prettypasswords.R
import org.json.JSONObject
import java.io.*
import java.nio.charset.StandardCharsets


// file operation related
// https://developer.android.com/training/data-storage/app-specific#kotlin

fun createCryptoFile(context: Context, eContent: JSONObject){

    val credential = PrettyManager.c!!

    println("eContenet at save file $eContent")


    val fileName = context.getResources().getString(R.string.cryptoFilePrefix) + credential.userName;
    val file = File(context.filesDir, fileName )
    println("file exists ${file.exists()}")

    try {

        if (file.exists()){
            deleteCryptoFile(context, fileName)
        }

        file.createNewFile()
        println("File created: " + file.name)

        // write content into file
        context.openFileOutput(fileName, Context.MODE_PRIVATE).use {
            it.write(eContent.toString().toByteArray())
        }

    } catch (e: IOException) {
        println("An error occurred.")
        e.printStackTrace()
    }


}



fun deleteCryptoFile(context: Context, fileName: String){


    val file = File(context.filesDir, fileName )

    val status = file.delete()
    println("File deleted: $fileName $status")

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