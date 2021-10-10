package com.prettypasswords.globals

import android.content.Context
import android.net.Uri
import android.util.Base64
import com.prettypasswords.model.*
import com.prettypasswords.utils.Encryption
import com.prettypasswords.utils.FileManager
import com.prettypasswords.utils.UserManager
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.lang.IllegalStateException
import kotlin.collections.ArrayList

object PrettyManager {

    const val sharedPreferenceKey = "PrettyManagerUserCredential"

    val e: Encryption by lazy {Encryption()}
    val u: UserManager by lazy {UserManager()}
    val f: FileManager by lazy {FileManager()}

    var b64ePws: String? = null

    // called by backupFragment
    fun saveAsFile(context: Context, uri: Uri): Boolean{
        val eContent = getEncryptedContent()
        return f.writeAsJsonFile(context, uri, eContent)
    }

    // register, to create credential and cryptoFile
    fun createUser(context: Context, userName: String, password: String){
        val c = UserManager.createCredential(context, userName, password)
        u.initWithCredential(context, c)
        f.createCryptoFile(context,
            fileName = f.getCryptoFileName(context, userName),
            eContent = c.toJson())
    }

    // call by splashFragment to decide go to signin or signup
    fun retrieveSavedFile(context: Context, userName: String): Boolean{
        val file = f.getFileWithUserName(context, userName) ?: return false
        val content = f.getFileContent(file) ?: return false
        return restoreCredential(content)
    }


    fun importFile(context: Context, uri: Uri): Boolean{

        val fileContent: JSONObject? = f.getUriContent(context, uri)
        if (fileContent == null || !restoreCredential(fileContent)){
            return false
        }
        val credential = u.credential?: throw IllegalStateException("credential is null")
        f.createCryptoFile(context,
            fileName = f.getCryptoFileName(context, credential.userName),
            eContent = fileContent)
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

    // if during change password, re-encrypt everything using the new credential, otherwise use the current encrypted content
    fun getEncryptedContent(c: Credential? = null): JSONObject{
        val credential = c?: u.credential?: throw IllegalStateException("credential is null")


        val content = JSONObject()
        content.put("credential", credential.toJson())

        if (b64ePws != null){
            val encryptedContent = b64ePws?: throw IllegalStateException("b64ePws is null")
            content.put("b64ePws", if (c == null) b64ePws else encryptPws(encryptedContent, c))
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

        f.createCryptoFile(context,
            fileName = f.getCryptoFileName(context, credential.userName),
            eContent = getEncryptedContent())
    }

    // turn password list to json list
    private fun encryptPws(pwList: List<Password>, sak: ByteArray): String {

        val jsonArray = JSONArray()
        for (pw in pwList){
            jsonArray.put(pw.toJson())
        }
        val bPws = jsonArray.toString().toByteArray()
        val ePws = e.sKeyEncrypt(bPws, sak)
        return Base64.encodeToString(ePws, Base64.DEFAULT)
    }

    private fun decryptPws(b64ePws: String? = null, sak: ByteArray): ArrayList<Password>{
        val pwList: ArrayList<Password> = ArrayList()

        if (b64ePws == null){
            return pwList
        }

        val ePws = Base64.decode(b64ePws, Base64.DEFAULT)

        val bPws = e.sKeyDecrypt(ePws, sak)

        val sPws = bPws.toString(Charsets.UTF_8)

        val jsonPws = JSONArray(sPws)

        for (i in 0 until jsonPws.length()) {
            val pw = jsonToPassword(jsonPws.getJSONObject(i))
            pwList.add(pw)
        }
        return pwList
    }

    // called during getEncryptedContent with password update
    // need to re-encrypt with new password
    private fun encryptPws(b64ePws: String, newCredential: Credential): String {
        val credential = u.credential?: throw IllegalStateException("credential is null")

        // decrypt with old credential
        val pwList = decryptPws(b64ePws, credential.getSak())

        // now encrypt with new credential
        return encryptPws(pwList, newCredential.getSak())
    }


    // called when user login success
    // if login success, the sak will be valid
    fun getDecryptedContent(): ArrayList<Password>{
        val credential = u.credential?: throw IllegalStateException("credential is null")
        return decryptPws(b64ePws, credential.getSak())
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


    fun updateProfile(context: Context, newUserName: String?, newPw: String?): Boolean{
        val credential = u.credential?: throw IllegalStateException("null credential")

        // first delete old file with old userName
        val file: File? = f.getFileWithUserName(context, credential.userName)
        f.deleteCryptoFile(file)

        val userName = newUserName?: credential.userName

        val credentialToSave = if (newPw != null)
            UserManager.createCredential(context, userName, newPw)
        else credential


        if (newUserName != null){
            credentialToSave.userName = newUserName
            u.saveCurrentUserName(context, newUserName)
        }

        val createSuccess = f.createCryptoFile(context,
            fileName = f.getCryptoFileName(context, credentialToSave.userName),
                    eContent = getEncryptedContent(credentialToSave))
        return createSuccess
    }


}