package com.prettypasswords.View.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.prettypasswords.R
import com.prettypasswords.Utilities.*
import com.prettypasswords.View.fragments.*

// MainActivity is just a container for signin or signup fragment
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)   // remove splash screen

        val ft = supportFragmentManager.beginTransaction()


        val userName: String? = getUserName(this)

        if (userName != null && !userName.equals("") && hasCredential(this,userName)){
            ft.replace(R.id.fragmentPlaceHolder, SignInFragment())
        }else{
            ft.replace(R.id.fragmentPlaceHolder, SignUpFragment())
        }
        ft.commit()

       setContentView(R.layout.activity_main);


    }
}
