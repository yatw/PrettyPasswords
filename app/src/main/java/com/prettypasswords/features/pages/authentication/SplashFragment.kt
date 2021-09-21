package com.prettypasswords.features.pages.authentication

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.prettypasswords.R
import com.prettypasswords.controller.getLastSessionUser
import com.prettypasswords.controller.hasCredential
import com.prettypasswords.databinding.FragmentSplashBinding
import com.prettypasswords.globals.PrettyManager

class SplashFragment: Fragment() {

    private lateinit var binding: FragmentSplashBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        val lastUser: String = getLastSessionUser(requireContext())
        if (lastUser != "" && hasCredential(requireContext(), lastUser)){
            if (PrettyManager.c!!.getSk() != null) {  // have credential and already login
                Handler(Looper.getMainLooper()).postDelayed({
                    findNavController().navigate(R.id.action_signInFragment_to_homeFragment)
                }, 800)
            }else{
                Handler(Looper.getMainLooper()).postDelayed({
                    findNavController().navigate(R.id.action_splashFragment_to_signInFragment)
                }, 800)
            }

        }else{
            // no credential, new user
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().navigate(R.id.action_splashFragment_to_signUpFragment)
            }, 800)
        }
    }
}