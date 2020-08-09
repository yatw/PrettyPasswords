package com.prettypasswords.view.activities


import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.prettypasswords.PrettyManager
import com.prettypasswords.R
import com.prettypasswords.controller.getLastSessionUser
import com.prettypasswords.controller.hasCredential
import com.prettypasswords.view.fragments.HomeFragment
import com.prettypasswords.view.fragments.SignInFragment
import com.prettypasswords.view.fragments.SignUpFragment
import kotlinx.android.synthetic.main.fragment_home.*
import java.lang.NullPointerException


// MainActivity is just a container for signin or signup fragment
class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)   // remove splash screen


        val lastUser: String? = getLastSessionUser(this)

        if (lastUser != null && lastUser != "" && hasCredential(
                this,
                lastUser
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

       setContentView(R.layout.activity_main)
    }


    fun replaceFragment(fragment: Fragment) {

        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()

        ft.replace(R.id.fragmentPlaceHolder, fragment)
        ft.commit()
    }


    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {

        try{
            if (drawer_layout.isDrawerOpen(GravityCompat.START)){
                drawer_layout.closeDrawer(GravityCompat.START)
                return
            }
        }catch (e: NullPointerException){
        }

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Tab again to exit", Toast.LENGTH_SHORT).show()

        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }


}
