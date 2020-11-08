package com.prettypasswords.view.fragments


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
import com.prettypasswords.controller.getLastSessionUser
import com.prettypasswords.controller.hasCredential
import com.prettypasswords.controller.loginByPassword
import com.prettypasswords.model.importFile
import com.prettypasswords.view.popups.showAlert
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

        }else{
            val lastUser: String = getLastSessionUser(activity!!)
            if (lastUser == ""){
                input_user_name.hint = "userName"
            }else{
                input_user_name.setText(lastUser)
            }
        }

        val ft = parentFragmentManager.beginTransaction()


        btn_import.setOnClickListener{

            ChooserDialog(activity)
                .withChosenListener(ChooserDialog.Result { path, file ->

                    if (importFile(activity!!, file)){
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
            ft.replace(R.id.fragmentPlaceHolder, SignUpFragment())
            ft.commit()
        }

        btn_signin.setOnClickListener{


            val userName = input_user_name.text.toString()
            val masterPassword = input_password.text.toString()


            if (userName == "" || masterPassword == ""){
                showAlert(
                    activity,
                    "Please enter credential",
                    ""
                )
            }else{

                if (!hasCredential(activity!!, userName)){
                    showAlert(
                        activity!!,
                        "No Account credential for {$userName}",
                        ""
                    )
                    return@setOnClickListener
                }

                if (loginByPassword(
                        activity!!,
                        userName,
                        masterPassword
                    )
                ){
                    Toast.makeText(activity, "Login Success", Toast.LENGTH_SHORT).show()
                    ft.replace(R.id.fragmentPlaceHolder, HomeFragment())
                    ft.commit()

                }else{
                    showAlert(
                        activity,
                        "Incorrect Credential",
                        ""
                    )
                }
            }
        }
    }
}
