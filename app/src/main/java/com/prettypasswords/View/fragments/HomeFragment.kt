package com.prettypasswords.View.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.prettypasswords.PrettyManager
import com.prettypasswords.R
import com.prettypasswords.Utilities.TagList
import com.prettypasswords.View.showAlert


class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val contentManager = PrettyManager.cm!!  // contentManager must be initialized after you have credential
        //contentManager.addTag(context!!, "babyishTag2", "babyforever2")

        println("getContent" + contentManager.getContent())
        println("body " + contentManager.getBody())

        //contentManager.decryptEntries("babyishTag2", "babyforever2")
        //println("tags " + contentManager.getTags())

        return TagList(context!!)
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