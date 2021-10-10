package com.prettypasswords.features.pages.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.prettypasswords.R
import com.prettypasswords.databinding.FragmentProfileBinding
import com.prettypasswords.globals.PrettyManager
import java.lang.IllegalStateException


class ProfileFragment: Fragment() {


    private val viewModel by lazy { ViewModelProvider(requireActivity()).get(ProfileViewModel::class.java) }
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Get a reference to the binding object and inflate the fragment views.
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        binding.vm = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSave.setOnClickListener {


            val success = viewModel.handleSave(requireContext())
            if (success){
                Toast.makeText(context, "Updated Success", Toast.LENGTH_SHORT).show()
                PrettyManager.u.logout(requireContext())
                goBackToSignIn()
            }else{
                Toast.makeText(context, "Updated Failed", Toast.LENGTH_LONG).show()
            }
        }

    }


    // Get out of nested nav graph
    // https://stackoverflow.com/a/65496919/5777189
    // parent of this fragment, which is home fragment hosting the drawer
    private fun goBackToSignIn(){
        var parent = parentFragment
        while (parent != null) {
            if (parent is HomeFragment) {
                val homeNavController = parent.findNavController()
                homeNavController.navigate(R.id.action_homeFragment_to_signInFragment)
                return
            }
            parent = parent.parentFragment
        }
        throw IllegalStateException("Unable to find parent navController")
    }

}