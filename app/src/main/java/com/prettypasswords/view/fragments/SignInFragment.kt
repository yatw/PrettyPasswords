package com.prettypasswords.view.fragments


import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.fragment.app.Fragment
import com.prettypasswords.PrettyManager
import com.prettypasswords.R
import com.prettypasswords.controller.getLastSessionUser
import com.prettypasswords.controller.hasCredential
import com.prettypasswords.controller.loginByPassword
import com.prettypasswords.model.importFile
import com.prettypasswords.view.popups.showAlert
import kotlinx.android.synthetic.main.fragment_signin.*


class SignInFragment// if success, must have restored credential
// populate username from credential
    () : Fragment() {


    lateinit  var mGetContent: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mGetContent = registerForActivityResult(GetContent(), ActivityResultCallback<Uri> { uri ->
            if (uri == null) {
                Toast.makeText(context, "No file selected", Toast.LENGTH_SHORT).show()
                return@ActivityResultCallback
            }else if (importFile(requireContext(), uri)){
                val credential = PrettyManager.c!!   // if success, must have restored credential
                input_user_name.setText(credential.userName)  // populate username from credential
            }
        })
    }

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

        val credential = PrettyManager.c
        if (credential != null) {
            input_user_name.setText(credential.userName)

        }else{
            val lastUser: String = getLastSessionUser(requireActivity())
            if (lastUser == ""){
                input_user_name.hint = "userName"
            }else{
                input_user_name.setText(lastUser)
            }
        }

        val ft = parentFragmentManager.beginTransaction()


        btn_import.setOnClickListener{
            mGetContent.launch("application/json")
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

                if (!hasCredential(requireActivity(), userName)){
                    showAlert(
                        requireActivity(),
                        "No Account credential for {$userName}",
                        ""
                    )
                    return@setOnClickListener
                }

                if (loginByPassword(
                        requireActivity(),
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
