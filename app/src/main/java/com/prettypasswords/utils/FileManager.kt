package com.prettypasswords.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.net.Uri
import com.prettypasswords.R
import com.prettypasswords.globals.PrettyManager
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import java.io.*
import java.nio.charset.StandardCharsets

class FileManager {


// file operation related
// https://developer.android.com/training/data-storage/app-specific#kotlin

    fun getCryptoFileName(context: Context): String{
        val credential = PrettyManager.u.credential!!
        return context.getResources().getString(R.string.cryptoFilePrefix) + credential.userName + ".json"
    }

    // overload for not having credential
    fun getCryptoFileName(context: Context, userName: String): String{
        return context.resources.getString(R.string.cryptoFilePrefix) + userName + ".json"
    }

    fun createCryptoFile(context: Context, eContent: JSONObject){

        val fileName = getCryptoFileName(context)
        val file = File(context.filesDir, fileName)

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

    fun writeContentToUri(activity: Activity, uri: Uri): Boolean{
        // write to specific file using uri
        //https://stackoverflow.com/questions/51490194/file-written-using-action-create-document-is-empty-on-google-drive-but-not-local
        val os: OutputStream? = activity.contentResolver.openOutputStream(uri)
        if (os != null) {
            val eContent = PrettyManager.getEncryptedContent()
            os.write(eContent.toString().toByteArray())
            os.close()
            return true
        }
        return false
    }

    fun getUriContent(context: Context, uri: Uri): JSONObject?{
        val cR = context.contentResolver
        val inputStream = cR.openInputStream(uri)

        // this dynamically extends to take the bytes you read
        val byteBuffer = ByteArrayOutputStream()

        // this is storage overwritten on each iteration with bytes
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)

        // we need to know how may bytes were read to write them to the byteBuffer
        var len = 0
        while (inputStream!!.read(buffer).also({ len = it }) != -1) {
            byteBuffer.write(buffer, 0, len)
        }

        // and then we can return your byte array.
        val data = byteBuffer.toByteArray()
        return try{
            val contents = String(data)
            JSONObject(contents)
        }catch (e: JSONException){
            null
        }
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
            JSONObject(contents)
        }catch (e: JSONException){
            null
        }

    }

    fun getFileWithUserName(context: Context, userName: String): File?{
        val predicatedName = getCryptoFileName(context, userName)
        val file = File(context.filesDir, predicatedName)
        return if (file.exists()) file else null
    }

    fun deleteCryptoFile(context: Context, fileName: String){
        val file = File(context.filesDir, fileName)
        val status = file.delete()
        //println("File deleted: $fileName status is $status")

    }

    fun deleteCryptoFile(file: File?){

        if (file != null){
            val status = file.delete()
            //println("File deleted: ${file.name} status is $status")
        }

    }


}