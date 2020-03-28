package com.prettypasswords.View.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.prettypasswords.PrettyManager
import com.prettypasswords.R
import com.prettypasswords.Utilities.getFileContent
import java.util.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)   // remove splash screen

        setContentView(R.layout.activity_home)


        PrettyManager.cm!!.decryptBody()
        println(PrettyManager.cm!!.content)
    }
}
