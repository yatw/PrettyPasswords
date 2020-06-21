package com.prettypasswords.view.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lxj.xpopup.XPopup
import com.prettypasswords.PrettyManager
import com.prettypasswords.R
import com.prettypasswords.model.Entry
import com.prettypasswords.model.Tag
import com.prettypasswords.view.components.AddEntryDialogue
import com.prettypasswords.view.components.EntryAdapter
import com.prettypasswords.view.components.TagAdapter
import kotlinx.android.synthetic.main.activity_entry_list.*
import kotlinx.android.synthetic.main.home_fragment.*


// display the entries under a tag
class EntriesListActivity : AppCompatActivity() {

    var context: Context = this


    var clickedTag = -1
    lateinit var tag: Tag
    lateinit var entryAdapter: EntryAdapter
    lateinit var listOfEntry: ArrayList<Entry>

    val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            //文件变化
            entryAdapter.notifyDataSetChanged()

            if (listOfEntry.size > 0){
                no_entries.visibility = View.GONE
                EntriesRecyclerView.visibility = View.VISIBLE
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)   // remove splash screen
        setContentView(R.layout.activity_entry_list)


        initData()
        initView()
        initClick()
    }

    private fun initData(){
        clickedTag = intent.getIntExtra("clickedTag", -1)
        tag = PrettyManager.cm!!.body.tags.get(clickedTag)
        listOfEntry = tag.entries
    }

    private fun initClick(){
        AddEntryBtn.setOnClickListener {
            XPopup.Builder(this).asCustom(AddEntryDialogue(this, tag)).show()
        }

    }

    private fun initView(){

        if (listOfEntry.size > 0){
            no_entries.visibility = View.GONE
            EntriesRecyclerView.visibility = View.VISIBLE
        }

        entryAdapter = EntryAdapter(this, tag.entries)

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
                context.startActivity(intent)

            }
        })
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, IntentFilter("addEntrySuccess"))
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
    }


}
