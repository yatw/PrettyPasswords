package com.prettypasswords.view.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.prettypasswords.PrettyManager
import com.prettypasswords.R
import kotlinx.android.synthetic.main.activity_back_up.*
import java.io.OutputStream


class BackUpActivity : AppCompatActivity() {

    lateinit var fileName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)   // remove splash screen
        setContentView(R.layout.activity_back_up)


        val credential = PrettyManager.c!!
        fileName = this.getResources().getString(R.string.cryptoFilePrefix) + credential.userName
        initClick()
        initView()
    }

    private fun initView(){


        var instructionsText = "File will be generated as ${fileName}\n"
        instructionsText += "\n1. Do not modify file name or file content, they will be validated during import\n"
        instructionsText += "\n2. Save this generated copy somewhere safe and do not lose it\n"

        label_instructions.text = instructionsText

    }

    private fun initClick(){

        btn_close.setOnClickListener {
            finish()
        }

        btn_select_folder.setOnClickListener{

            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "text/plain"
                putExtra(Intent.EXTRA_TITLE, fileName)

                // Optionally, specify a URI for the directory that should be opened in
                // the system file picker before your app creates the document.
                //putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri)
            }
            startActivityForResult(intent, 600)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode==600 && resultCode == Activity.RESULT_OK){

            label_save_result.visibility = View.VISIBLE
            var fileWritten = false

            val uri = data!!.data

            if (uri != null) {


                // write to specific file using uri
                //https://stackoverflow.com/questions/51490194/file-written-using-action-create-document-is-empty-on-google-drive-but-not-local
                val os: OutputStream? = getContentResolver().openOutputStream(uri)
                if (os != null) {
                    val eContent = PrettyManager.cm!!.getEncryptedContent()
                    os.write(eContent.toString().toByteArray())
                    os.close()
                    fileWritten = true

                    label_save_result.text = "Success, File ${fileName} saved at path ${uri.path}"
                }

            }

            if (!fileWritten){
                label_save_result.text = "Error occured, File not saved"
            }


        }
    }

}