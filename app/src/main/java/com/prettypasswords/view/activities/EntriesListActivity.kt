package com.prettypasswords.view.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.lxj.xpopup.XPopup
import com.prettypasswords.PrettyManager
import com.prettypasswords.R
import com.prettypasswords.model.Tag
import com.prettypasswords.view.components.AddEntryDialogue
import com.prettypasswords.view.components.AddTagDialogue
import kotlinx.android.synthetic.main.entries_list_fragment.*
import kotlinx.android.synthetic.main.home_fragment.*


class EntriesListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)   // remove splash screen


        val tagPosition: Int = intent.extras!!.get("tagPosition") as Int;

        val tag: Tag = PrettyManager.cm!!.body.tags[tagPosition]



        setContentView(R.layout.entries_list_fragment)

        EntryList.initData(tag)

        AddEntryBtn.setOnClickListener {
            XPopup.Builder(this).asCustom(AddEntryDialogue(this, tag)).show()
        }
    }

}
