package com.prettypasswords.view.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lxj.xpopup.XPopup
import com.prettypasswords.PrettyManager
import com.prettypasswords.R
import com.prettypasswords.contants.*
import com.prettypasswords.model.Entry
import com.prettypasswords.model.Tag
import kotlinx.android.synthetic.main.activity_entry.*


class EntryActivity : AppCompatActivity() {

    lateinit var tag: Tag
    lateinit var entry: Entry
    private var clickedEntry: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)   // remove splash screen

        setContentView(R.layout.activity_entry);


        val clickedTag = intent.getIntExtra("clickedTag", -1)
        clickedEntry = intent.getIntExtra("clickedEntry", -1)

        tag = PrettyManager.cm!!.body.tags.get(clickedTag)
        entry = tag.entries.get(clickedEntry)

        initView()
        initClick()

    }

    @SuppressLint("SetTextI18n")
    fun initView(){

        subtitle_text.text = "Entry in tag ${tag.tagName}"
        siteNameEdit.setText(entry.siteName)
        userNameEdit.setText(entry.userName)
        passwordEdit.setText(entry.password)
        emailEdit.setText(entry.email)
        othersEdit.setText(entry.others)
    }

    fun initClick(){


        back_button.setOnClickListener {
            finish()
        }

        save_button.setOnClickListener{
            saveEntry()
        }

        save_button2.setOnClickListener{
            saveEntry()
        }

        //delete_button
        delete_button.setOnClickListener {

            // 显示确认和取消对话框
            // https://github.com/li-xiaojun/XPopup/wiki/2.-%E5%86%85%E7%BD%AE%E7%9A%84%E5%BC%B9%E7%AA%97%E5%AE%9E%E7%8E%B0

            XPopup.Builder(this).asConfirm(
                "Delete entry ${entry.siteName}?", ""
            ) {

                entry.delete(this)
                val intent = Intent()
                intent.putExtra("clickedEntry", clickedEntry)
                setResult(DELETE_ENTRY, intent)
                finish()

            }.show()


        }


    }

    private fun saveEntry(){

        val siteName = siteNameEdit.text.toString()
        val userName = userNameEdit.text.toString()
        val password = passwordEdit.text.toString()
        val email = emailEdit.text.toString()
        val others = othersEdit.text.toString()

        entry.save(this, siteName, userName, password, email, others)
        Toast.makeText(this, "Saved", Toast.LENGTH_LONG).show()

        val intent = Intent()
        intent.putExtra("clickedEntry", clickedEntry)
        setResult(EDIT_ENTRY,intent)
        finish()
    }
}
