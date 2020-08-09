package com.prettypasswords.view.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.enums.PopupAnimation
import com.prettypasswords.PrettyManager
import com.prettypasswords.R
import com.prettypasswords.contants.*
import com.prettypasswords.controller.logout
import com.prettypasswords.view.activities.*
import com.prettypasswords.view.components.TagAdapter
import com.prettypasswords.view.popups.AddTag
import com.prettypasswords.view.popups.DecryptTag
import com.prettypasswords.view.popups.QuickMessage
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.layout_drawer.*
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
        return inflater.inflate(R.layout.fragment_home, container, false)
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

        btn_drawer.setOnClickListener {

            if (drawer_layout.isDrawerOpen(GravityCompat.START)){
                drawer_layout.closeDrawer(GravityCompat.START)
            }else{
                drawer_layout.openDrawer(GravityCompat.START)
            }
        }


        // drawer item
        nav_view.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.btn_manage_profile -> {

                    val intent = Intent(context, ProfileActivity::class.java)
                    startActivityForResult(intent,DISPLAY_MANAGE_PROFILE)
                }
                R.id.btn_back_up -> {

                    val intent = Intent(context, BackUpActivity::class.java)
                    startActivity(intent)
                }
                R.id.btn_source_code -> {

                    val repo_url = resources.getString(R.string.repo_url)

                    val intent = Intent(context, BrowserActivity::class.java)
                    intent.putExtra("title", "Source Code")
                    intent.putExtra("url", repo_url)
                    startActivity(intent)
                }
                R.id.btn_how_this_work -> {

                    val wiki_url = resources.getString(R.string.wiki_url)

                    val intent = Intent(context, BrowserActivity::class.java)
                    intent.putExtra("title", "How this work")
                    intent.putExtra("url", wiki_url)
                    startActivity(intent)
                }
                R.id.btn_faq -> {

                    XPopup.Builder(context)
                        .offsetX(100)
                        .offsetY(600)
                        .popupAnimation(PopupAnimation.TranslateAlphaFromRight)
                        .asCustom(QuickMessage(context!!, "Wasn't expecting any FAQ, \nthanks for clicking anyway"))
                        .show()

                }
                R.id.btn_bug -> {

                    XPopup.Builder(context)
                        .offsetX(100)
                        .offsetY(800)
                        .popupAnimation(PopupAnimation.TranslateFromBottom)
                        .asCustom(QuickMessage(context!!, "It's not a bug, it's a feature !!"))
                        .show()
                }
            }
            //drawer_layout.closeDrawer(GravityCompat.START)
            true
        }

        button_logout.setOnClickListener {

            // 显示确认和取消对话框
            // https://github.com/li-xiaojun/XPopup/wiki/2.-%E5%86%85%E7%BD%AE%E7%9A%84%E5%BC%B9%E7%AA%97%E5%AE%9E%E7%8E%B0

            XPopup.Builder(context).asConfirm(
                "Logout of ${PrettyManager.c!!.userName}?", ""
            ) {

                logout(context!!)
                (activity as MainActivity).replaceFragment(SignInFragment())  // switch fragment back to signin

                Toast.makeText(context, "Log out Success", Toast.LENGTH_LONG).show()

            }.show()

        }
    }

    private fun initView(){


        // set user name in drawer
        val headerView = nav_view.getHeaderView(0)
        val txtUsername: TextView = headerView.findViewById(R.id.nav_username)
        txtUsername.text = PrettyManager.c!!.userName

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

                val tag = tags.get(position)

                if (!tag.decrypted()){

                    XPopup.Builder(context).asCustom(
                        DecryptTag(
                            context!!,
                            position,
                            this@HomeFragment
                        )
                    ).show()

                }else{

                    val intent = Intent(context, TagActivity::class.java)
                    intent.putExtra("clickedTag", position)
                    startActivityForResult(intent, DISPLAY_TAG)

                }
            }
        })


    }

    private fun notifyItemRemoved(pos: Int){

        tagAdapter.notifyItemRemoved(pos)

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
        if (requestCode==DISPLAY_TAG && resultCode==DELETE_TAG && data!=null){

            val clickedTag = data.getIntExtra("clickedTag", -1)

            if (clickedTag >= 0){
                notifyItemRemoved(clickedTag)
            }

            // logout required from ProfileActivity
        }else if (requestCode==DISPLAY_MANAGE_PROFILE && resultCode == LOGOUT_REQUIRED){

            logout(context!!)
            (activity as MainActivity).replaceFragment(SignInFragment())  // switch fragment back to signin

        }
    }

}