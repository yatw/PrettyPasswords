package com.yatw.prettypasswords.features.pages.authentication

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableBoolean
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.yatw.prettypasswords.R
import com.yatw.prettypasswords.databinding.FragmentSignupBinding
import com.yatw.prettypasswords.globals.PrettyManager


class SignUpFragment: Fragment() {

    private lateinit var binding: FragmentSignupBinding

    val userNameHasInput = ObservableBoolean(false)
    val pw1HasInput = ObservableBoolean(false)
    val pw2HasInput = ObservableBoolean(false)


    private val activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.OpenDocument(),
        ActivityResultCallback { uri ->
            if (uri == null){
                Toast.makeText(requireContext(), "Canceled", Toast.LENGTH_SHORT).show()
                return@ActivityResultCallback
            }

            val importSuccess = PrettyManager.importFile(requireContext(), uri)
            if (importSuccess){
                Toast.makeText(requireContext(), "Import file Success!", Toast.LENGTH_SHORT).show()
                requireActivity().findNavController(R.id.main_nav_host).navigate(R.id.action_signUpFragment_to_signInFragment)
            }else{
                Toast.makeText(requireContext(), "Import file Failed!", Toast.LENGTH_SHORT).show()
            }
        }
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_signup, container, false)
        binding.fragment = this
        binding.lifecycleOwner = this
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindObservableWithTextListener(binding.inputUserName, userNameHasInput)
        bindObservableWithTextListener(binding.inputPassword, pw1HasInput)
        bindObservableWithTextListener(binding.inputPassword2, pw2HasInput)

        binding.btnSignup.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {

                val userName = binding.inputUserName.text.toString()
                val masterPassword = binding.inputPassword.text.toString()

                PrettyManager.createUser(requireActivity().applicationContext, userName, masterPassword)
                Toast.makeText(context, "User $userName created", Toast.LENGTH_SHORT).show()
                requireActivity().findNavController(R.id.main_nav_host).navigate(R.id.action_signUpFragment_to_homeFragment)
            }
        })

        binding.btnImport.setOnClickListener {
            activityResultLauncher.launch(arrayOf("application/json"))
        }

    }

    private fun bindObservableWithTextListener(editText: EditText, observableBoolean: ObservableBoolean){
        editText.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                observableBoolean.set(!s.isNullOrBlank())
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

}