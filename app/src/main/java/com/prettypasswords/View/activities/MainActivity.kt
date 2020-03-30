package com.prettypasswords.View.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.prettypasswords.PrettyManager
import com.prettypasswords.R
import com.prettypasswords.Utilities.getUserName
import com.prettypasswords.Utilities.hasCredential
import com.prettypasswords.View.fragments.HomeFragment
import com.prettypasswords.View.fragments.SignInFragment
import com.prettypasswords.View.fragments.SignUpFragment


// MainActivity is just a container for signin or signup fragment
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)   // remove splash screen


        val userName: String? = getUserName(this)

        if (userName != null && !userName.equals("") && hasCredential(this,userName)){

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

}
