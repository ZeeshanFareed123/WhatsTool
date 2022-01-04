package com.mobi.dslrguru.sideBarDrawer

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.R
import com.google.android.material.navigation.NavigationView
import de.hdodenhof.circleimageview.CircleImageView


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
open class BaseActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        TODO("Not yet implemented")
    }
    /* internal var drawer: DrawerLayout? = null
     internal var navigationView: NavigationView? = null

     @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
     @SuppressLint("WrongConstant")
     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.base_navigation)


         drawer = findViewById(R.id.drawer_layout) as DrawerLayout
         navigationView = findViewById(R.id.nav_view) as NavigationView
         val header: View = navigationView!!.getHeaderView(0)
         drawer!!.addDrawerListener(object : DrawerLayout.DrawerListener {
             override fun onDrawerStateChanged(newState: Int) {
             }

             override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
             }

             override fun onDrawerClosed(drawerView: View) {
             }

             override fun onDrawerOpened(drawerView: View) {

             }
         })

     }


     override fun onNavigationItemSelected(@NonNull item: MenuItem): Boolean {
         val id = item.itemId
 *//*        if (id == R.id.About) {
            val intent = Intent(applicationContext, About::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            overridePendingTransition (0, 0)
        } else if (id == R.id.Terms) {
            val intent = Intent(applicationContext, Term::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            overridePendingTransition (0, 0)
        } else if (id == R.id.Privacy) {
            val intent = Intent(applicationContext, Privacy_policy::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            overridePendingTransition (0, 0)
        } else if (id == R.id.Share) {
            share("https://play.google.com/store/apps/details?id=$packageName")
        } *//*

        drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        drawer!!.closeDrawer(GravityCompat.START)
        return true
    }*/

}
