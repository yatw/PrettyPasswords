package com.prettypasswords.view.activities

import android.content.res.Configuration
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.prettypasswords.PrettyManager
import com.prettypasswords.R
import com.prettypasswords.controller.getUserName
import com.prettypasswords.controller.hasCredential
import com.prettypasswords.view.fragments.HomeFragment
import com.prettypasswords.view.fragments.SignInFragment
import com.prettypasswords.view.fragments.SignUpFragment


// MainActivity is just a container for signin or signup fragment
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)   // remove splash screen


        val userName: String? = getUserName(this)

        if (userName != null && !userName.equals("") && hasCredential(
                this,
                userName
            )
        ){

            if (PrettyManager.c!!.getSk() != null){  // have credential and already login
                replaceFragment(HomeFragment())
            }else{
                replaceFragment(SignInFragment()) // have credential but haven't login
            }

        }else{  // no credential, new user
            replaceFragment(SignUpFragment())
        }

       setContentView(R.layout.activity_main);
    }


    fun replaceFragment(fragment: Fragment) {

        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()

        ft.replace(R.id.fragmentPlaceHolder, fragment)
        ft.commit()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            //这里写需要重写的方法
            return false
        }
        return super.onKeyDown(keyCode, event)
    }

}
