package com.yatw.prettypasswords.features.pages.content

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.yatw.prettypasswords.databinding.FragmentBackupBinding
import com.yatw.prettypasswords.globals.PrettyManager


class BackupFragment: Fragment() {

    private lateinit var binding: FragmentBackupBinding
    private lateinit var fileName: String


    private val activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.CreateDocument(),
        ActivityResultCallback { uri ->
            if (uri == null){
                Toast.makeText(requireContext(), "Canceled", Toast.LENGTH_SHORT).show()
                return@ActivityResultCallback
            }
            binding.labelSaveResult.visibility = View.VISIBLE

            val success = PrettyManager.saveAsFile(requireActivity(), uri)
            if (success){
                binding.labelSaveResult.setTextColor(Color.GREEN)
                binding.labelSaveResult.text = "Success, File ${fileName} saved at path ${uri.path}"
            }else{
                binding.labelSaveResult.setTextColor(Color.RED)
                binding.labelSaveResult.text = "Error occured, File not saved"
            }
        }
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBackupBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fileName = PrettyManager.f.getCryptoFileName(requireContext(),
            userName = PrettyManager.u.getUserName())

        binding.labelInstructions.text = """
            File will be generated as ${fileName}
            
            1. Do not modify file name or file content, they will be validated during import
            2. Save this generated copy somewhere safe and do not lose it
        """.trimIndent()

        binding.btnSelectFolder.setOnClickListener {
            activityResultLauncher.launch(fileName)
        }

    }

}