package com.prettypasswords.view.activities

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.lxj.xpopup.XPopup
import com.prettypasswords.PrettyManager
import com.prettypasswords.R
import com.prettypasswords.model.Entry
import com.prettypasswords.model.Tag
import com.prettypasswords.view.components.AddEntryDialogue
import com.prettypasswords.view.components.ChangeTagPasswordPopUp
import com.prettypasswords.view.components.EntryAdapter
import kotlinx.android.synthetic.main.activity_entry_list.*


// display the entries under a tag
class EntriesListActivity : AppCompatActivity() {

    var context: Context = this


    var clickedTag = -1
    lateinit var tag: Tag
    lateinit var entryAdapter: EntryAdapter
    lateinit var listOfEntry: ArrayList<Entry>


    val addEntryReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            //文件变化
            entryAdapter.notifyDataSetChanged()

            if (listOfEntry.size > 0){
                no_entries.visibility = View.GONE
                EntriesRecyclerView.visibility = View.VISIBLE
                EntryCount.text = "${listOfEntry.size} entries"
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


        setting_btn.setOnClickListener{

            // https://github.com/li-xiaojun/XPopup/wiki/2.-%E5%86%85%E7%BD%AE%E7%9A%84%E5%BC%B9%E7%AA%97%E5%AE%9E%E7%8E%B0
            // 这种弹窗从 1.0.0版本开始实现了优雅的手势交互和智能嵌套滚动
            XPopup.Builder(this)
                .asCenterList(
                    "Manage tag ${tag.tagName}",

                    arrayOf(
                        "Add entry",
                        "Rename tag",
                        "Change tag password",
                        "Delete tag")
                ) {
                        position, text ->

                    when (position) {

                        0 -> XPopup.Builder(this).asCustom(AddEntryDialogue(this, tag)).show()

                        1 -> renameTagPopUp()

                        2 -> changeTagPasswordPopUp()

                        3 -> deleteTagConfirm()
                    }

                }
                .show()


        }

    }

    @SuppressLint("SetTextI18n")
    private fun initView(){

        if (listOfEntry.size > 0){
            no_entries.visibility = View.GONE
            EntriesRecyclerView.visibility = View.VISIBLE
        }

        TagNameLabel.text = tag.tagName
        EntryCount.text = "${listOfEntry.size} entries"

        entryAdapter = EntryAdapter(this, tag.entries)

        EntriesRecyclerView.adapter = entryAdapter
        EntriesRecyclerView.layoutManager = LinearLayoutManager(this)



        // implement what happen when a edit button is clicked
        entryAdapter.setItemClickListener(object : EntryAdapter.ItemClickListener{

            override fun onItemClick(view: View?, position: Int) {

                val intent = Intent(context, EntryActivity::class.java)
                intent.putExtra("clickedTag", clickedTag)
                intent.putExtra("clickedEntry", position)
                startActivityForResult(intent,20)

            }
        })



        // implement what happen when the whole card view is long clicked
        entryAdapter.setStickClickListener(object : EntryAdapter.StickClickListener{

            override fun onItemClick(builder: XPopup.Builder, position: Int): Boolean {

                builder.asAttachList(
                    arrayOf("Edit","Delete"), null
                ) {
                        pos, text ->

                        if (text == "Edit"){

                            val intent = Intent(context, EntryActivity::class.java)
                            intent.putExtra("clickedTag", clickedTag)
                            intent.putExtra("clickedEntry", position)
                            startActivityForResult(intent,20)

                        }else if (text == "Delete"){

                            val entry =  listOfEntry.get(position)

                            // 显示确认和取消对话框
                            // https://github.com/li-xiaojun/XPopup/wiki/2.-%E5%86%85%E7%BD%AE%E7%9A%84%E5%BC%B9%E7%AA%97%E5%AE%9E%E7%8E%B0

                            XPopup.Builder(context).asConfirm(
                                "Confirm delete", "You sure you want to delete ${entry.siteName}?"
                            ) {

                                entry.delete(context)
                                notifyItemRemoved(position)


                            }.show()

                        }else{
                            Toast.makeText(context, text, Toast.LENGTH_LONG).show()
                        }

                }
                    .show()
                return false
            }
        })
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(addEntryReceiver, IntentFilter("addEntrySuccess"))
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(addEntryReceiver)
    }

    private fun notifyItemRemoved(pos: Int){

        entryAdapter.notifyItemRemoved(pos);


        if (listOfEntry.size == 0){
            no_entries.visibility = View.VISIBLE
            EntriesRecyclerView.visibility = View.GONE
        }

        EntryCount.text = "${listOfEntry.size} entries"

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        // entry content edited
        if (requestCode==20 && resultCode==2 && data!=null){

            val clickedEntry = data.getIntExtra("clickedEntry", -1)

            if (clickedEntry >= 0){
                entryAdapter.notifyItemChanged(clickedEntry)
            }

        // one entry was deleted
        }else if (requestCode==20 && resultCode==4 && data!=null){

            val position = data.getIntExtra("clickedEntry", -1)

            notifyItemRemoved(position)

        }
    }

    private fun renameTagPopUp(){

        XPopup.Builder(context)
            .autoOpenSoftInput(true)
            .asInputConfirm("Rename tag ${tag.tagName}", "", "Enter new tag name")
            {

                input ->  tag.renameTag(context, input)

                // update title
                TagNameLabel.text = tag.tagName

                // notify the list to update ui
                val updateIntent = Intent("tagStatusChanged")
                updateIntent.putExtra("clickedTag", clickedTag)
                LocalBroadcastManager.getInstance(context).sendBroadcast(updateIntent)

            }
            .show()

    }

    private fun changeTagPasswordPopUp(){
        XPopup.Builder(context)
            .autoOpenSoftInput(true)
            .asCustom(ChangeTagPasswordPopUp(context, tag))
            .show()
    }

    private fun deleteTagConfirm(){


        // 显示确认和取消对话框
        // https://github.com/li-xiaojun/XPopup/wiki/2.-%E5%86%85%E7%BD%AE%E7%9A%84%E5%BC%B9%E7%AA%97%E5%AE%9E%E7%8E%B0

        XPopup.Builder(this).asConfirm(
            "Delete Tag ${tag.tagName}?", "All ${tag.entries.size} entries in this tag will be deleted"
        ) {

            tag.delete(context)

            val intent = Intent()
            intent.putExtra("clickedTag", clickedTag)
            setResult(6, intent)
            finish()

        }.show()

    }

}
