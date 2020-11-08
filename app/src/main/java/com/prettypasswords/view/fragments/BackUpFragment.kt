package com.prettypasswords.view.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.prettypasswords.PrettyManager
import com.prettypasswords.R
import com.prettypasswords.contants.GENERATE_SAVE_FILE
import kotlinx.android.synthetic.main.fragment_back_up.*
import kotlinx.android.synthetic.main.fragment_browser.btn_close
import java.io.OutputStream


class BackUpFragment : Fragment() {

    lateinit var fileName: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val credential = PrettyManager.c!!
        fileName = this.getResources().getString(R.string.cryptoFilePrefix) + credential.userName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_back_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initClick()
    }


    private fun initView(){
        var instructionsText = "File will be generated as ${fileName}\n"
        instructionsText += "\n1. Do not modify file name or file content, they will be validated during import\n"
        instructionsText += "\n2. Save this generated copy somewhere safe and do not lose it\n"
        label_instructions.text = instructionsText
    }

    private fun initClick(){

        btn_close.setOnClickListener {
            parentFragmentManager.popBackStack()
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
            this.startActivityForResult(intent, GENERATE_SAVE_FILE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
       // super.onActivityResult(requestCode, resultCode, data)

        if (requestCode==GENERATE_SAVE_FILE && resultCode == Activity.RESULT_OK){

            label_save_result.visibility = View.VISIBLE
            var fileWritten = false

            val uri = data!!.data

            if (uri != null) {


                // write to specific file using uri
                //https://stackoverflow.com/questions/51490194/file-written-using-action-create-document-is-empty-on-google-drive-but-not-local
                val os: OutputStream? = activity!!.contentResolver.openOutputStream(uri)
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