package com.prettypasswords.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.prettypasswords.R
import com.prettypasswords.controller.createUser
import com.prettypasswords.view.activities.MainActivity
import com.prettypasswords.view.popups.showAlert
import kotlinx.android.synthetic.main.fragment_signup.*


class SignUpFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false)

    }

    // set stored userName to userName field
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_goto_signin.setOnClickListener{
            (activity as MainActivity?)!!.replaceFragment(SignInFragment())  // switch fragment back to signin
        }

        btn_signup.setOnClickListener{

            val userName = input_user_name.text.toString()
            val masterPassword = input_password.text.toString()

            if (userName.equals("") || masterPassword.equals("")){
                showAlert(
                    getActivity(),
                    "Plase enter credential",
                    ""
                )
            }else{
                createUser(
                    getActivity()!!.getApplicationContext(),
                    userName,
                    masterPassword
                )
                Toast.makeText(context, "User $userName created", Toast.LENGTH_SHORT).show()

                // switch fragment to home fragment
                (activity as MainActivity?)!!.replaceFragment(HomeFragment())
            }

        }

    }
}
