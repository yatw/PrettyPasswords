package com.prettypasswords.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.lxj.xpopup.XPopup
import com.prettypasswords.R
import com.prettypasswords.controller.*
import com.prettypasswords.view.popups.showAlert
import kotlinx.android.synthetic.main.fragment_browser.btn_close
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClick()
    }

    private fun initClick(){

        btn_close.setOnClickListener {
            parentFragmentManager.popBackStack()
        }


        check_update_username.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                username_group.visibility = View.VISIBLE
            } else {
                username_group.visibility = View.GONE
            }

            btn_save.visibility = if ((check_update_username.isChecked || check_update_master_pw.isChecked)) View.VISIBLE else View.GONE

        })

        check_update_master_pw.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                master_pw_group.visibility = View.VISIBLE
            } else {
                master_pw_group.visibility = View.GONE
            }

            btn_save.visibility = if ((check_update_username.isChecked || check_update_master_pw.isChecked)) View.VISIBLE else View.GONE
        })


        btn_save.setOnClickListener {


            val updateUserName = check_update_username.isChecked
            val updatePassword = check_update_master_pw.isChecked

            val userName: String = new_user_name.getText().toString()
            val curPw: String = input_current_pw.getText().toString()
            val new_pw1: String = input_new_pw1.getText().toString()
            val new_pw2: String = input_new_pw2.getText().toString()


            if (updateUserName && updatePassword){

                if (checkInputName(userName) && checkInputPw(curPw, new_pw1, new_pw2)) {

                    XPopup.Builder(activity).asConfirm(
                        "Continue?", "This will cause you to logout"
                    ) {
                        changeUserNameAndPassword(activity!!, userName, new_pw1)
                        handleLogout()

                    }.show()
                }

            }else if (updatePassword){

                if (checkInputPw(curPw, new_pw1, new_pw2)) {

                    XPopup.Builder(activity).asConfirm(
                        "Continue?", "This will cause you to logout"
                    ) {
                        changePassword(activity!!, new_pw1)
                        handleLogout()

                    }.show()
                }


            }else if (updateUserName){

                if (checkInputName(userName)){
                    XPopup.Builder(activity).asConfirm(
                        "Continue?", "This will cause you to logout"
                    ) {
                        changeUserName(activity!!, userName)
                        handleLogout()

                    }.show()
                }
            }
        }
    }

    private fun handleLogout(){

        val backStackCount = parentFragmentManager.backStackEntryCount
        for (i in 0 until backStackCount) {
            parentFragmentManager.popBackStack()
        }

        val ft: FragmentTransaction = parentFragmentManager.beginTransaction()
        ft.replace(R.id.fragmentPlaceHolder, SignInFragment())
        ft.commit()

        logout(activity!!)
    }

    private fun checkInputName(userName: String): Boolean{

        if (userName.isEmpty()){
            showAlert(
                activity,
                "Error",
                "userName Cannot be empty"
            )
            return false
        }
        return true
    }

    private fun checkInputPw(curPw: String, new_pw1: String, new_pw2: String): Boolean{

        if (curPw.isEmpty() || new_pw1.isEmpty() || new_pw2.isEmpty()){
            showAlert(
                activity,
                "Error",
                "Input Cannot be empty"
            )
            return false
        }else if (new_pw1 != new_pw2){
            showAlert(
                activity,
                "Error",
                "New Passwords do not match"
            )
            return false
        }else if (new_pw1 == curPw){
            showAlert(
                activity,
                "Error",
                "Why bother changing"
            )
            return false
        }else if (!validatePassword(curPw)){
            showAlert(
                activity,
                "Error",
                "Incorrect password"
            )
            return false
        }

        return true
    }

}