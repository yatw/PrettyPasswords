package com.prettypasswords.view.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lxj.xpopup.XPopup
import com.prettypasswords.PrettyManager
import com.prettypasswords.R
import com.prettypasswords.model.Tag
import com.prettypasswords.view.activities.EntriesListActivity
import com.prettypasswords.view.components.AddTagDialogue
import com.prettypasswords.view.components.DecryptTagDialogue
import com.prettypasswords.view.components.TagAdapter
import kotlinx.android.synthetic.main.home_fragment.*


class HomeFragment : Fragment() {

    val listOfTag = PrettyManager.cm!!.body.tags
    lateinit var tagAdapter: TagAdapter


    val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            //文件变化
            tagAdapter.notifyDataSetChanged()

            if (listOfTag.size > 0){
                no_tags.visibility = View.GONE
                TagsRecyclerView.visibility = View.VISIBLE
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
            XPopup.Builder(context).asCustom(AddTagDialogue(context!!)).show()
        }
    }

    private fun initView(){

        if (listOfTag.size > 0){
            no_tags.visibility = View.GONE
            TagsRecyclerView.visibility = View.VISIBLE
        }


        tagAdapter = TagAdapter(context!!, listOfTag)


        TagsRecyclerView.adapter = tagAdapter
        TagsRecyclerView.layoutManager = LinearLayoutManager(context!!)

        // add divider between each item
        TagsRecyclerView.addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))


        // implement what happen when a tag is clicked
        tagAdapter.setItemClickListener(object : TagAdapter.ItemClickListener{

            override fun onItemClick(view: View?, position: Int) {

                val tag = tagAdapter.tags.get(position);

                if (!tag.decrypted()){

                    XPopup.Builder(context).asCustom(DecryptTagDialogue(context!!, position)).show()

                }else{

                    Toast.makeText(context, "$position tag is already decrypted", Toast.LENGTH_LONG).show()

                    val intent = Intent(context, EntriesListActivity::class.java)
                    intent.putExtra("clickedTag", position)
                    context!!.startActivity(intent)

                }
            }
        })


    }


    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(context!!).registerReceiver(receiver, IntentFilter("addTagSuccess"))
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(context!!).unregisterReceiver(receiver)
    }

/*
    // follow this design
// https://dribbble.com/shots/2353448-VK-Player-music-Android-App
    fun generateTagList(context: Context): RecyclerView{


        if (sectionBody == null){
            showAlert(context, "Body is encrypted", "cannot display tag body is encrypted ")
        }

        val tags = tempContent.getJSONArray("tags")




        // set up the RecyclerView
        val recyclerView: RecyclerView = RecyclerView(context)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = MyRecyclerViewAdapter(this, animalNames)
        //adapter.setClickListener(this)
        recyclerView.adapter = adapter
    }
*/



}