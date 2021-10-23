package com.yatw.prettypasswords.features.pages.authentication

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.yatw.prettypasswords.R
import com.yatw.prettypasswords.databinding.FragmentSplashBinding
import com.yatw.prettypasswords.globals.PrettyManager

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

        val lastUser: String = PrettyManager.u.getLastSessionUserName(requireContext())
        if (lastUser != "" && PrettyManager.retrieveSavedFile(requireContext(), lastUser)){
            val credential = PrettyManager.u.credential

            if (credential != null && credential.hasSk()) {  // have credential and already login
                Handler(Looper.getMainLooper()).postDelayed({
                    findNavController().navigate(R.id.action_splashFragment_to_homeActivity)
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