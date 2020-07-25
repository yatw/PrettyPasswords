package com.prettypasswords.view.fragments


import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.obsez.android.lib.filechooser.ChooserDialog
import com.prettypasswords.PrettyManager
import com.prettypasswords.R
import com.prettypasswords.controller.hasCredential
import com.prettypasswords.controller.loginByPassword
import com.prettypasswords.model.importFile
import com.prettypasswords.utilities.showAlert
import com.prettypasswords.view.activities.MainActivity
import kotlinx.android.synthetic.main.fragment_signin.*


class SignInFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signin, container, false)
    }


    // set stored userName to userName field
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var credential = PrettyManager.c

        if (credential != null) {
            input_user_name.setText(credential.userName)
        }

        val context: Context? = getActivity()


        btn_import.setOnClickListener{

            ChooserDialog(getContext())
                .withChosenListener(ChooserDialog.Result { path, file ->

                    if (importFile(getContext()!!, file)){
                        credential = PrettyManager.c!!   // if success, must have restored credential
                        input_user_name.setText(credential!!.userName)  // populate username from credential
                    }

                }) // to handle the back key pressed or clicked outside the dialog:
                .withOnCancelListener(DialogInterface.OnCancelListener { dialog ->
                    dialog.cancel() // MUST have
                })
                .build()
                .show()

        }

        btn_goto_signup.setOnClickListener{
            (activity as MainActivity?)!!.replaceFragment(SignUpFragment())  // switch fragment back to signup
        }

        btn_signin.setOnClickListener{


            val userName = input_user_name.text.toString()
            val masterPassword = input_password.text.toString()


            if (userName.equals("") || masterPassword.equals("")){
                showAlert(getActivity(), "Plase enter credential","")
            }else{

                if (!hasCredential(getContext()!!, userName)){
                    showAlert(getActivity(), "No Account credential","")
                    return@setOnClickListener
                }

                if (loginByPassword(
                        context!!,
                        userName,
                        masterPassword
                    )
                ){
                    Toast.makeText(context, "Login Success", Toast.LENGTH_SHORT).show()

                    // switch fragment to home fragment
                    (activity as MainActivity?)!!.replaceFragment(HomeFragment())

                }else{
                    showAlert(context, "Incorrect Credential", "")
                }
            }


        }


    }
}
