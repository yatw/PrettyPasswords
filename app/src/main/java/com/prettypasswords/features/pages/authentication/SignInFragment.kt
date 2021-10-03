package com.prettypasswords.features.pages.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.prettypasswords.R
import com.prettypasswords.databinding.FragmentSigninBinding
import com.prettypasswords.globals.PrettyManager
import com.prettypasswords.utils.showAlert
import java.lang.IllegalStateException

class SignInFragment: Fragment() {

    private lateinit var binding: FragmentSigninBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSigninBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val credential = PrettyManager.u.credential
            ?: throw IllegalStateException("No Credential for login")

        binding.inputUserName.setText(credential.userName)

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

    }
}