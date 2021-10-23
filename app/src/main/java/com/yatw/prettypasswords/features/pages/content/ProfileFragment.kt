package com.yatw.prettypasswords.features.pages.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.yatw.prettypasswords.R
import com.yatw.prettypasswords.databinding.FragmentProfileBinding
import com.yatw.prettypasswords.globals.PrettyManager

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
                requireActivity().finishAffinity() // exit app
            }else{
                Toast.makeText(context, "Updated Failed", Toast.LENGTH_LONG).show()
            }
        }

    }

}