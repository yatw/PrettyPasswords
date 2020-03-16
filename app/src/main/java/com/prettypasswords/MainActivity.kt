package com.prettypasswords

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.prettypasswords.Utilities.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userName = "babythestorm"
        val pw = "testing123"

        deleteUser(this, userName)

        createUser(this, "babythestorm", "testing123")

        val success = loginByPassword(this,"babythestorm", "testing123")
        println("login success is $success")

        val content = getFileContent(this, userName)
        println(content)
    }
}
