package com.prettypasswords.View.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.prettypasswords.PrettyManager
import com.prettypasswords.R
import kotlinx.android.synthetic.main.sign_in_fragment.*


class SignInFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.sign_in_fragment, container, false)
    }


    // set stored userName to input field
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val credential = PrettyManager.c

        if (credential != null) {
            userNameInput.setText(credential.userName)
        }
    }
}
