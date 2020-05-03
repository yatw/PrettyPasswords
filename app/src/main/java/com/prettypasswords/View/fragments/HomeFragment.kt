package com.prettypasswords.View.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.lxj.xpopup.XPopup
import com.prettypasswords.PrettyManager
import com.prettypasswords.R
import com.prettypasswords.View.components.CustomPopup
import kotlinx.android.synthetic.main.home_fragment.*


class HomeFragment : Fragment() {

    val contentManager = PrettyManager.cm!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

          // contentManager must be initialized after you have credential
        //contentManager.addTag(context!!, "babyishTag2", "babyforever2")

        println("getContent" + contentManager.getContent())
        println("body " + contentManager.getBody())

        //contentManager.decryptEntries("babyishTag2", "babyforever2")
        //println("tags " + contentManager.getTags())

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Instead of view.findViewById(R.id.hello) as TextView


        AddTagBtn.setOnClickListener {

            XPopup.Builder(context)
                .asCustom(CustomPopup(context!!))
                .show()
        }
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