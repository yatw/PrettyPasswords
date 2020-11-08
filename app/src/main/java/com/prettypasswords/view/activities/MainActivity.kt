package com.prettypasswords.view.activities


import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
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


// MainActivity is just a container for different fragment
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)   // remove splash screen

        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()

        val lastUser: String = getLastSessionUser(this)

        if (lastUser != "" && hasCredential(
                this,
                lastUser
            )
        ){

            if (PrettyManager.c!!.getSk() != null){  // have credential and already login
                ft.replace(R.id.fragmentPlaceHolder, HomeFragment())
                ft.commit()
            }else{
                ft.replace(R.id.fragmentPlaceHolder, SignInFragment()) // have credential but haven't login
                ft.commit()
            }

        }else{  // no credential, new user
            ft.replace(R.id.fragmentPlaceHolder, SignUpFragment())
            ft.commit()
        }

       setContentView(R.layout.activity_main)
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

        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        }else{
            this.doubleBackToExitPressedOnce = true
            Toast.makeText(this, "Tab again to exit", Toast.LENGTH_SHORT).show()
            Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
        }
    }

}
