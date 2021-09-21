package com.prettypasswords.features.pages.authentication

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.prettypasswords.R
import com.prettypasswords.controller.hasCredential
import com.prettypasswords.controller.loginByPassword
import com.prettypasswords.databinding.FragmentSigninBinding
import com.prettypasswords.features.pages.MainViewModel
import com.prettypasswords.globals.PrettyManager
import com.prettypasswords.utils.showAlert

class SignInFragment: Fragment() {

    private lateinit var binding: FragmentSigninBinding

    // Use the 'by activityViewModels()' Kotlin property delegate
    // from the fragment-ktx artifact
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSigninBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignin.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {

                val userName = binding.inputUserName.text.toString()
                val masterPassword = binding.inputPassword.text.toString()

                if (userName == "" || masterPassword == ""){
                    showAlert(activity, "Please enter credential", "")
                }else{

                    if (!hasCredential(requireActivity(), userName)){
                        showAlert(requireActivity(), "No Account credential for {$userName}", "")
                        return
                    }

                    if (loginByPassword(requireActivity(), userName, masterPassword)){
                        Toast.makeText(activity, "Login Success", Toast.LENGTH_SHORT).show()
                        requireActivity().findNavController(R.id.main_nav_host).navigate(R.id.action_signInFragment_to_homeFragment)
                    }else{
                        showAlert(activity, "Incorrect Credential", "")
                    }
                }
            }
        })

    }
}