package com.prettypasswords.View.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.prettypasswords.R
import com.prettypasswords.Utilities.createUser
import com.prettypasswords.View.activities.HomeActivity
import com.prettypasswords.View.showAlert
import kotlinx.android.synthetic.main.sign_up_fragment.*


class SignUpFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.sign_up_fragment, container, false)

    }

    // set stored userName to userName field
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signUpButton.setOnClickListener{

            val userName = userNameInput.text.toString()
            val masterPassword = mpwInput.text.toString()

            if (userName.equals("") || masterPassword.equals("")){
                showAlert(getActivity(), "Plase enter credential","")
            }else{
                createUser(getActivity()!!.getApplicationContext(), userName, masterPassword)
                Toast.makeText(context, "User $userName created", Toast.LENGTH_SHORT).show()

                val intent = Intent(context, HomeActivity::class.java)
                startActivity(intent)
            }

        }

    }
}
