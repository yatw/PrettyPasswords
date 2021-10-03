package com.prettypasswords.features.pages.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.prettypasswords.R
import com.prettypasswords.databinding.FragmentSignupBinding
import com.prettypasswords.globals.PrettyManager
import com.prettypasswords.utils.showAlert

class SignUpFragment: Fragment() {

    private lateinit var binding: FragmentSignupBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignup.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {

                val userName = binding.inputUserName.text.toString()
                val masterPassword = binding.inputPassword.text.toString()

                if (userName == "" || masterPassword == ""){
                    showAlert(activity, "Plase enter credential", "")
                }else{
                    PrettyManager.createUser(requireActivity().applicationContext, userName, masterPassword)
                    Toast.makeText(context, "User $userName created", Toast.LENGTH_SHORT).show()
                    requireActivity().findNavController(R.id.main_nav_host).navigate(R.id.action_signUpFragment_to_homeFragment)
                }
            }
        })

        binding.btnGotoSignin.setOnClickListener {
            requireActivity().findNavController(R.id.main_nav_host).navigate(R.id.action_signUpFragment_to_signInFragment)
        }
    }

}