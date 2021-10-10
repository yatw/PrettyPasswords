package com.prettypasswords.features.pages.authentication

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.prettypasswords.R
import com.prettypasswords.databinding.FragmentSigninBinding
import com.prettypasswords.globals.PrettyManager
import com.prettypasswords.utils.showAlert
import java.lang.IllegalStateException

class SignInFragment: Fragment() {

    private lateinit var binding: FragmentSigninBinding

    private val activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.OpenDocument(),
        ActivityResultCallback { uri ->
            if (uri == null){
                Toast.makeText(requireContext(), "Canceled", Toast.LENGTH_SHORT).show()
                return@ActivityResultCallback
            }

            val importSuccess = PrettyManager.importFile(requireContext(), uri)
            if (importSuccess){
                fillInUserName()
                Toast.makeText(requireContext(), "Import file Success!", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(requireContext(), "Import file Failed!", Toast.LENGTH_SHORT).show()
            }
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSigninBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun fillInUserName(){
        val credential = PrettyManager.u.credential
            ?: throw IllegalStateException("No Credential for login")

        binding.inputUserName.setText(credential.userName)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fillInUserName()

        binding.btnSignin.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {

                val masterPassword = binding.inputPassword.text.toString()

                if (PrettyManager.u.loginByPassword(requireActivity(), masterPassword)){
                    Toast.makeText(activity, "Login Success", Toast.LENGTH_SHORT).show()
                    requireActivity().findNavController(R.id.main_nav_host).navigate(R.id.action_signInFragment_to_homeFragment)
                }else{
                    showAlert(activity, "Incorrect Credential", "")
                }
            }
        })

        binding.inputPassword.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.btnSignin.isEnabled = !s.isNullOrEmpty()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.btnImport.setOnClickListener {
            activityResultLauncher.launch(arrayOf("application/json"))
        }

    }

}