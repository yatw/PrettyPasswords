package com.prettypasswords.view.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lxj.xpopup.XPopup
import com.prettypasswords.PrettyManager
import com.prettypasswords.R
import com.prettypasswords.model.Tag
import com.prettypasswords.view.components.AddEntryDialogue
import com.prettypasswords.view.components.EntryAdapter
import kotlinx.android.synthetic.main.activity_entry_list.*
import java.io.Serializable


// display the entries under a tag
class EntriesListActivity : AppCompatActivity() {

    var context: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)   // remove splash screen
        setContentView(R.layout.activity_entry_list)

        context = this



        setUpRecyclerView()
    }


    private fun setUpRecyclerView(){


        val clickedTag = intent.getIntExtra("clickedTag", -1)
        val tag = PrettyManager.cm!!.body.tags.get(clickedTag)



        AddEntryBtn.setOnClickListener {
            XPopup.Builder(this).asCustom(AddEntryDialogue(this, tag)).show()
        }




        var entryAdapter = EntryAdapter(this, tag.entries)

        EntriesRecyclerView.adapter = entryAdapter
        EntriesRecyclerView.layoutManager = LinearLayoutManager(this)

        // add divider between each item
        EntriesRecyclerView.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL))



        // implement what happen when a tag is clicked
        entryAdapter.setItemClickListener(object : EntryAdapter.ItemClickListener{

            override fun onItemClick(view: View?, position: Int) {

                val intent = Intent(context, EntryActivity::class.java)
                intent.putExtra("clickedTag", clickedTag)
                intent.putExtra("clickedEntry", position)
                context!!.startActivity(intent)

            }
        })
    }



}
