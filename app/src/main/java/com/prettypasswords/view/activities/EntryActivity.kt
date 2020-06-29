package com.prettypasswords.view.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.prettypasswords.PrettyManager
import com.prettypasswords.R
import com.prettypasswords.model.Entry
import kotlinx.android.synthetic.main.activity_entry.*


class EntryActivity : AppCompatActivity() {


    lateinit var entry: Entry

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)   // remove splash screen

        setContentView(R.layout.activity_entry);


        val clickedTag = intent.getIntExtra("clickedTag", -1)
        val clickedEntry = intent.getIntExtra("clickedEntry", -1)

        val tag = PrettyManager.cm!!.body.tags.get(clickedTag)
        entry = tag.entries.get(clickedEntry)

        initView()
        initClick()

    }

    fun initView(){

/*        tag_title.setText("This entry belong to tag ${entry.parentTag.tagName}")
        site.setText(entry.name)
        content_edittext.setText(entry.getContent())*/
    }

    fun initClick(){


        back_button.setOnClickListener {
            //返回
            finish()
        }

        //save_button
        save_button.setOnClickListener{
            Toast.makeText(this, "save", Toast.LENGTH_LONG).show()


            val content = content_edittext.text.toString()
            println(content)


            //entry.save(this, content )

        }

        //delete_button
        delete_button.setOnClickListener {
            Toast.makeText(this, "delete", Toast.LENGTH_LONG).show()
        }

        //new_button
        new_button.setOnClickListener {
            Toast.makeText(this, "new", Toast.LENGTH_LONG).show()

        }

    }
}
