package com.prettypasswords.features.pages.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.prettypasswords.R
import com.prettypasswords.databinding.FragmentHomeBinding
import kotlinx.android.synthetic.main.fragment_drawer.view.*


class HomeFragment: Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        // bind the drawer with the navigation graph

        // I used  findNavController() doesn't work, see
        //https://stackoverflow.com/questions/65375389/how-to-set-bottomnavigationview-inside-fragment
        val navHostFragment = childFragmentManager.findFragmentById(R.id.nav_host_home) as NavHostFragment
        val navController = navHostFragment.navController
        binding.drawerLayout.nav_view.setupWithNavController(navController)
        //binding.navView.setupWithNavController(navController)

        // bind the toolbar with navigation graph
        val appBarConfiguration = AppBarConfiguration(
            navGraph = navController.graph,
            drawerLayout = binding.drawerLayout
        )
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

    }

}