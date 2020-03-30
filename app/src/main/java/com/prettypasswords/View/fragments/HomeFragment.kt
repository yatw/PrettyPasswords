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
        contentManager.decryptBody()

        println(contentManager.tempContent)


        val context = activity






        return view
    }


}