package com.yatw.prettypasswords.features.pages.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.yatw.prettypasswords.databinding.FragmentBrowserBinding

abstract class BrowserFragment: Fragment() {

    protected lateinit var binding: FragmentBrowserBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBrowserBinding.inflate(inflater, container, false)
        return binding.root
    }
}