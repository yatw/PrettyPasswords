package com.prettypasswords.View.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lxj.xpopup.XPopup
import com.prettypasswords.R
import com.prettypasswords.View.components.AddTagDialogue
import kotlinx.android.synthetic.main.home_fragment.*


class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        AddTagBtn.setOnClickListener {
            XPopup.Builder(context).asCustom(AddTagDialogue(context!!)).show()
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