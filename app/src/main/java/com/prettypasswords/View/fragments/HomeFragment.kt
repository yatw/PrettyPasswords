package com.prettypasswords.View.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.prettypasswords.PrettyManager
import com.prettypasswords.R


class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.home_fragment, container, false)


        val contentManager = PrettyManager.cm!!  // contentManager must be initialized after you have credential
        //contentManager.addTag(context!!, "babyishTag2", "babyforever2")

        println("getContent" + contentManager.getContent())
        println("body " + contentManager.getBody())

        contentManager.decryptEntries("babyishTag2", "babyforever2")
        println("tags " + contentManager.getTags())


        return view
    }


}