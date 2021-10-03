package com.prettypasswords.features.pages.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.prettypasswords.R
import com.prettypasswords.databinding.FragmentPwEditBinding
import com.prettypasswords.model.Password
import timber.log.Timber


class PwEditFragment: Fragment() {

    private val args: PwEditFragmentArgs by navArgs()

    private lateinit var binding: FragmentPwEditBinding

    private val viewModel: PwViewModel by navGraphViewModels(R.id.nav_password)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPwEditBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val password = viewModel.getPassword(args.position)
        Timber.i("$password")

        binding.sitenameInput.setText(password.siteName)
        binding.userNameInput.setText(password.userName)
        binding.passwordInput.setText(password.password)
        binding.emailInput.setText(password.email)
        binding.othersInput.setText(password.others)

        binding.saveButton.setOnClickListener {

            val siteName = binding.sitenameInput.text.toString()
            val userName = binding.userNameInput.text.toString()
            val email = binding.emailInput.text.toString()
            val pw = binding.passwordInput.text.toString()
            val others = binding.othersInput.text.toString()

            val p = Password(siteName, userName, email, pw, others)
            viewModel.updatePassword(requireContext(), args.position, p)

            Toast.makeText(requireContext(), "Saved", Toast.LENGTH_SHORT).show()

            findNavController().navigateUp()
        }

        binding.deleteButton.setOnClickListener {

            viewModel.deletePassword(requireContext(), args.position)
            findNavController().navigateUp()
            Toast.makeText(requireContext(), "Deleted", Toast.LENGTH_SHORT).show()

        }

    }
}