package com.prettypasswords.view.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import com.lxj.xpopup.XPopup
import com.prettypasswords.R
import com.prettypasswords.controller.*
import com.prettypasswords.utilities.showAlert
import kotlinx.android.synthetic.main.activity_profile.*


class ProfileActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)   // remove splash screen

        setContentView(R.layout.activity_profile)

        initClick()

    }

    private fun initClick(){

        btn_close.setOnClickListener {
            finish()
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

                    XPopup.Builder(this).asConfirm(
                        "Continue?", "This will cause you to logout"
                    ) {
                        changeUserNameAndPassword(this, userName, new_pw1)

                        val intent = Intent()
                        setResult(44, intent)
                        finish()

                    }.show()
                }

            }else if (updatePassword){

                if (checkInputPw(curPw, new_pw1, new_pw2)) {

                    XPopup.Builder(this).asConfirm(
                        "Continue?", "This will cause you to logout"
                    ) {
                        changePassword(this, new_pw1)

                        val intent = Intent()
                        setResult(44, intent)
                        finish()

                    }.show()
                }


            }else if (updateUserName){

                if (checkInputName(userName)){
                    XPopup.Builder(this).asConfirm(
                        "Continue?", "This will cause you to logout"
                    ) {
                        changeUserName(this, userName)

                        val intent = Intent()
                        setResult(44, intent)
                        finish()

                    }.show()
                }
            }

        }

    }

    private fun checkInputName(userName: String): Boolean{

        if (userName.isEmpty()){
            showAlert(this, "Error", "userName Cannot be empty")
            return false
        }
        return true
    }

    private fun checkInputPw(curPw: String, new_pw1: String, new_pw2: String): Boolean{

        if (curPw.isEmpty() || new_pw1.isEmpty() || new_pw2.isEmpty()){
            showAlert(this, "Error", "Input Cannot be empty")
            return false
        }else if (new_pw1 != new_pw2){
            showAlert(this, "Error", "New Passwords do not match")
            return false
        }else if (new_pw1 == curPw){
            showAlert(this, "Error", "Why bother changing")
            return false
        }else if (!validatePassword(curPw)){
            showAlert(this, "Error", "Incorrect password")
            return false
        }

        return true
    }
}