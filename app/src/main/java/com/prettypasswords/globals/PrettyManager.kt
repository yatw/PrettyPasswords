package com.prettypasswords.globals

import android.content.Context
import android.net.Uri
import android.util.Base64
import com.prettypasswords.model.*
import com.prettypasswords.utils.Encryption
import com.prettypasswords.utils.FileManager
import com.prettypasswords.utils.UserManager
import com.prettypasswords.utils.showAlert
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import java.lang.IllegalStateException
import kotlin.collections.ArrayList


// Singleton
object PrettyManager {

    const val sharedPreferenceKey = "PrettyManagerUserCredential"

    val e: Encryption by lazy {Encryption()}
    val u: UserManager by lazy {UserManager()}
    val f: FileManager by lazy {FileManager()}

    var b64ePws: String? = null

    // register, to create cryptoFile
    fun createUser(context: Context, userName: String, password: String){
        val c = u.createCredential(context, userName, password)
        f.createCryptoFile(context, c.toJson())
    }

    fun retrieveSavedFile(context: Context, userName: String): Boolean{
        val file = f.getFileWithUserName(context, userName) ?: return false
        val content = f.getFileContent(file) ?: return false
        Timber.i("retrieved content")
        Timber.i(content.toString())
        return restoreCredential(content)
    }


    fun importFile(context: Context, uri: Uri): Boolean{

        val fileContent: JSONObject? = f.getUriContent(context, uri)
        if (fileContent == null){
            showAlert(
                context,
                "Get fileContent failed",
                "Probably not in json format"
            )
            return false
        }

        if (!restoreCredential(fileContent)){
            showAlert(
                context,
                "ValidateFile failed",
                ""
            )
            return false
        }

        f.createCryptoFile(context, fileContent)

        showAlert(
            context,
            "Import File Success",
            "You can now access this account"
        )
        return true
    }


    private fun restoreCredential(fileContent: JSONObject): Boolean{

        try{
            val fileCredential = fileContent.getJSONObject("credential")
            val userName = fileCredential.getString("userName")
            val b64esk = fileCredential.getString("b64esk")
            val b64pk = fileCredential.getString("b64pk")
            val b64xesak = fileCredential.getString("b64xesak")

            val pk: ByteArray = Base64.decode(b64pk, Base64.DEFAULT)
            val esk: ByteArray = Base64.decode(b64esk, Base64.DEFAULT)
            val xesak: ByteArray = Base64.decode(b64xesak, Base64.DEFAULT)

            val credential = Credential(userName = userName, pk = pk, esk = esk, xesak = xesak)
            u.credential = credential

            if (fileContent.has("b64ePws")){
                b64ePws = fileContent.getString("b64ePws")
            }

            return true
        }catch(e: JSONException){
            e.printStackTrace()
        }
        return false
    }

    fun getEncryptedContent(): JSONObject{
        val credential = u.credential?: throw IllegalStateException("credential is null")

        val content = JSONObject()
        content.put("credential", credential)
        if (b64ePws != null){
            content.put("b64ePws", b64ePws)
        }
        return content
    }

    @JvmStatic
    fun savePasswords(context: Context, list: List<Password>){
        val credential = u.credential?: throw IllegalStateException("credential is null")

        b64ePws = encryptPws(
            list,
            credential.getSak()
        )

        f.createCryptoFile(context, getEncryptedContent())
    }

    // turn password list to json list
    private fun encryptPws(pwList: List<Password>, sak: ByteArray): String? {

        val jsonArray = JSONArray()
        for (pw in pwList){
            jsonArray.put(pw.toJson())
        }
        val bPws = jsonArray.toString().toByteArray()
        val ePws = e.sKeyEncrypt(bPws, sak)
        return Base64.encodeToString(ePws, Base64.DEFAULT)
    }


    // called when user login success
    // if login success, the sak will be valid
    fun decrypt(): ArrayList<Password>{
        val c = u.credential ?: throw IllegalStateException("credential is null")

        val pwList: ArrayList<Password> = ArrayList()

        if (b64ePws == null){
            return pwList
        }


        val ePws = Base64.decode(b64ePws, Base64.DEFAULT)

        val bPws = e.sKeyDecrypt(ePws, c.getSak())

        val sPws = bPws.toString(Charsets.UTF_8)

        val jsonPws = JSONArray(sPws)

        // store all tags
        for (i in 0 until jsonPws.length()) {
            val pw = jsonToPassword(jsonPws.getJSONObject(i))
            pwList.add(pw)
        }
        return pwList
    }

    private fun jsonToPassword(pwJson: JSONObject): Password {

        val siteName = pwJson.getString("siteName")
        val userName = pwJson.getString("userName")
        val email = pwJson.getString("email")
        val password = pwJson.getString("password")
        val others = if (pwJson.has("others")) pwJson.getString("others") else null
        val lastModified = pwJson.getString("lastModified")
        return Password(siteName, userName, email, password, others, lastModified)
    }


}