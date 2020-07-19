package com.prettypasswords.view.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lxj.xpopup.XPopup
import com.prettypasswords.PrettyManager
import com.prettypasswords.R
import com.prettypasswords.view.activities.EntriesListActivity
import com.prettypasswords.view.popups.AddTag
import com.prettypasswords.view.popups.DecryptTag
import com.prettypasswords.view.components.TagAdapter
import kotlinx.android.synthetic.main.layout_home_main.*


class HomeFragment : Fragment() {

    val tags = PrettyManager.cm!!.body.tags
    lateinit var tagAdapter: TagAdapter


    val addTagReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            //文件变化
            tagAdapter.notifyDataSetChanged()

            if (tags.size > 0){
                no_tags.visibility = View.GONE
                TagsRecyclerView.visibility = View.VISIBLE
            }

        }
    }

    // when decrypt success, or tag name change, ui need update
    val tagStatusChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            if (intent != null){
                val pos: Int = intent.getIntExtra("clickedTag", -1)
                if (pos >= 0){
                    tagAdapter.notifyItemChanged(pos);
                }
            }
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initClick()
    }


    private fun initClick(){
        AddTagBtn.setOnClickListener {
            XPopup.Builder(context).asCustom(
                AddTag(
                    context!!
                )
            ).show()
        }
    }

    private fun initView(){

        if (tags.size > 0){
            no_tags.visibility = View.GONE
            TagsRecyclerView.visibility = View.VISIBLE
        }


        tagAdapter = TagAdapter(context!!, tags)


        TagsRecyclerView.adapter = tagAdapter
        TagsRecyclerView.layoutManager = LinearLayoutManager(context!!)

        // add divider between each item
        TagsRecyclerView.addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))


        // implement what happen when a tag is clicked
        tagAdapter.setItemClickListener(object : TagAdapter.ItemClickListener{

            override fun onItemClick(view: View?, position: Int) {

                val tag = tags.get(position);

                if (!tag.decrypted()){

                    XPopup.Builder(context).asCustom(
                        DecryptTag(
                            context!!,
                            position,
                            this@HomeFragment
                        )
                    ).show()

                }else{

                    val intent = Intent(context, EntriesListActivity::class.java)
                    intent.putExtra("clickedTag", position)
                    startActivityForResult(intent,10)

                }
            }
        })


    }

    private fun notifyItemRemoved(pos: Int){

        tagAdapter.notifyItemRemoved(pos);

        if (tags.size == 0){
            no_tags.visibility = View.VISIBLE
            TagsRecyclerView.visibility = View.GONE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocalBroadcastManager.getInstance(context!!).registerReceiver(addTagReceiver, IntentFilter("addTagSuccess"))
        LocalBroadcastManager.getInstance(context!!).registerReceiver(tagStatusChangeReceiver, IntentFilter("tagStatusChanged"))
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(context!!).unregisterReceiver(addTagReceiver)
        LocalBroadcastManager.getInstance(context!!).unregisterReceiver(tagStatusChangeReceiver)
    }

    /*
        We can call startActivityForResult directly from Fragment but actually mechanic behind are all handled by Activity.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //super.onActivityResult(requestCode, resultCode, data)  comment this unless you want to pass your result to the activity

        // a tag was deleted
        if (requestCode==10 && resultCode==6 && data!=null){

            val clickedTag = data.getIntExtra("clickedTag", -1)

            if (clickedTag >= 0){
                notifyItemRemoved(clickedTag)
            }

        }
    }



}