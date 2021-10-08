package com.prettypasswords.features.pages.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.prettypasswords.R
import com.prettypasswords.databinding.FragmentProfileBinding


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

        binding.checkUpdateUsername.isChecked = viewModel.userNameChecked.value?: false
        binding.checkUpdateMasterPw.isChecked = viewModel.updatePasswordChecked.value?: false

        binding.btnSave.setOnClickListener {

        }

    }

}