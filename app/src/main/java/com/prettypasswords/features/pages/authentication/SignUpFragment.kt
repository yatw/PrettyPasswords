package com.prettypasswords.features.pages.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.prettypasswords.R
import com.prettypasswords.databinding.FragmentSigninBinding
import com.prettypasswords.databinding.FragmentSignupBinding

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
                requireActivity().findNavController(R.id.main_nav_host).navigate(R.id.action_signUpFragment_to_signInFragment)
            }

        })
    }

}