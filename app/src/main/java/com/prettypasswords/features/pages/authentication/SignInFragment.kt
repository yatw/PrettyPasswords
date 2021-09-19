package com.prettypasswords.features.pages.authentication

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.prettypasswords.R
import com.prettypasswords.databinding.FragmentSigninBinding

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

        binding.btnSignin.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                requireActivity().findNavController(R.id.main_nav_host).navigate(R.id.action_signInFragment_to_homeFragment)
            }
        })
    }

}