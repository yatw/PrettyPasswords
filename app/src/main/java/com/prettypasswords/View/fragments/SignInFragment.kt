package com.prettypasswords.View.fragments


import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.prettypasswords.PrettyManager
import com.prettypasswords.R
import com.prettypasswords.Utilities.loginByPassword
import com.prettypasswords.View.activities.HomeActivity
import com.prettypasswords.View.showAlert
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


    // set stored userName to userName field
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val credential = PrettyManager.c

        if (credential != null) {
            userNameInput.setText(credential.userName)
        }

        val context: Context? = getActivity()

        signInButton.setOnClickListener{

            val userName = userNameInput.text.toString()
            val masterPassword = mpwInput.text.toString()


            if (userName.equals("") || masterPassword.equals("")){
                showAlert(getActivity(), "Plase enter credential","")
            }else{

                if (loginByPassword(context!!, userName, masterPassword)){

                    Toast.makeText(context, "Login Success", Toast.LENGTH_SHORT).show()

                    val intent = Intent(context,HomeActivity::class.java)
                    startActivity(intent)

                }else{
                    showAlert(context, "Incorrect Credential", "")
                }
            }


        }
    }
}
