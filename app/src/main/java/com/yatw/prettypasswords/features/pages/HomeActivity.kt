package com.yatw.prettypasswords.features.pages

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.enums.PopupAnimation
import com.yatw.prettypasswords.R
import com.yatw.prettypasswords.databinding.ActivityHomeBinding
import com.yatw.prettypasswords.globals.PrettyManager
import com.yatw.prettypasswords.view.popups.QuickMessage
import kotlinx.android.synthetic.main.activity_home.view.*


class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val header = binding.navView.getHeaderView(0)
        val userName = header.findViewById<TextView>(R.id.nav_username)
        userName.text = PrettyManager.u.getUserName()

        // bind the drawer with the navigation graph

        // findNavController() never worked for me, see
        //https://stackoverflow.com/questions/65375389/how-to-set-bottomnavigationview-inside-fragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_home) as NavHostFragment
        navController = navHostFragment.navController


        // Don't set up drawer layout with navController, unable to handle system back click close drawer
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
                    XPopup.Builder(this)
                        .offsetX(100)
                        .offsetY(200)
                        .popupAnimation(PopupAnimation.TranslateAlphaFromRight)
                        .asCustom(QuickMessage(this, "Wasn't expecting any FAQ, \nthanks for clicking anyway"))
                        .show()
                }
                R.id.btn_bug -> {
                    XPopup.Builder(this)
                        .offsetX(100)
                        .offsetY(400)
                        .popupAnimation(PopupAnimation.TranslateFromBottom)
                        .asCustom(QuickMessage(this, "It's not a bug, it's a feature !!"))
                        .show()
                }

            }

            binding.drawerLayout.closeDrawer(GravityCompat.START)
            return@setNavigationItemSelectedListener true
        }

        binding.btnLogout.setOnClickListener {
            PrettyManager.u.logout(this)
            Toast.makeText(this, "You have logout!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }


    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)){
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            return
        }
        if (navController.previousBackStackEntry != null){
            navController.navigateUp()
            return
        }
        super.onBackPressed() // need this to invoke PwListFragment's onBackPressedDispatcher.addCallback
    }

}