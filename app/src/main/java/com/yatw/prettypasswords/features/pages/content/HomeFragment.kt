package com.yatw.prettypasswords.features.pages.content

import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.enums.PopupAnimation
import com.yatw.prettypasswords.R
import com.yatw.prettypasswords.databinding.FragmentHomeBinding
import com.yatw.prettypasswords.globals.PrettyManager
import com.yatw.prettypasswords.view.popups.QuickMessage
import kotlinx.android.synthetic.main.fragment_home.view.*


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

        // bind the toolbar with navigation graph
        val appBarConfiguration = AppBarConfiguration(
            navGraph = navController.graph,
            drawerLayout = binding.drawerLayout
        )

        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.navView.setNavigationItemSelectedListener{ item: MenuItem ->

            // You need this line to handle the navigation
            val handled = NavigationUI.onNavDestinationSelected(item, navController)
            when (item.itemId) {
                R.id.btn_faq -> {
                    XPopup.Builder(context)
                        .offsetX(100)
                        .offsetY(200)
                        .popupAnimation(PopupAnimation.TranslateAlphaFromRight)
                        .asCustom(QuickMessage(requireContext(), "Wasn't expecting any FAQ, \nthanks for clicking anyway"))
                        .show()
                }
                R.id.btn_bug -> {
                    XPopup.Builder(context)
                        .offsetX(100)
                        .offsetY(400)
                        .popupAnimation(PopupAnimation.TranslateFromBottom)
                        .asCustom(QuickMessage(requireContext(), "It's not a bug, it's a feature !!"))
                        .show()
                }

            }

            binding.drawerLayout.closeDrawer(GravityCompat.START)
            return@setNavigationItemSelectedListener true
        }

        binding.btnLogout.setOnClickListener {
            PrettyManager.u.logout(requireContext())
            findNavController().navigate(R.id.action_homeFragment_to_signInFragment)
            Toast.makeText(requireContext(), "You have logout!", Toast.LENGTH_SHORT).show()
        }

        val header = binding.navView.getHeaderView(0)
        val userName = header.findViewById<TextView>(R.id.nav_username)
        userName.text = PrettyManager.u.getUserName()
    }

}