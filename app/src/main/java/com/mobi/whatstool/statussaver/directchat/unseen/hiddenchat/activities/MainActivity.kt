package com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.activities

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.R
import com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.adapter.Pager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.base_navigation.*
import saveit.whatsappstatussaver.whatsappsaver.Utils.CommonUtils.Companion.ChatAlladapter
import saveit.whatsappstatussaver.whatsappsaver.Utils.CommonUtils.Companion.tabPosition


class MainActivity  : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{
    @SuppressLint("WrongConstant")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_navigation)

       // val inflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
       // @SuppressLint("InflateParams")
       // val contentView = inflater.inflate(R.layout.activity_main, null, false)
      //  drawer!!.addView(contentView, 0)

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.mediumseagreen))
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.mediumseagreen))
        }

       this.supportActionBar!!.hide();
        tablayout!!.addTab(tablayout!!.newTab().setText("    Chat     "))
        tablayout!!.addTab(tablayout!!.newTab().setText("Deleted Media"))
        tablayout!!.addTab(tablayout!!.newTab().setText("   Status     "))
        tablayout!!.addTab(tablayout!!.newTab().setText(" Downloaded  "))
        tablayout!!.addTab(tablayout!!.newTab().setText(" Direct Chat "))

        /*val theString = "Zeeshan-fareed"
        val parts: List<String> = theString.split("-")
        val first = parts[0]
        val second = parts[1]
        Log.i("stringPart: " ,first )
        Log.i("stringPart: " ,second )*/

        val adapter = Pager(this, supportFragmentManager, tablayout!!.tabCount)
        pager!!.setAdapter(adapter)
        pager.setOffscreenPageLimit(5)

        pager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tablayout))
        tablayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                pager!!.currentItem = tab.position
                tabPosition = tab.position
                if(tabPosition==0){
                    search.visibility= View.VISIBLE

                }
                else{
                    ChatAlladapter!!.filter!!.filter("")
                    search.visibility= View.GONE
                    tp_toolbarSearch.visibility= View.GONE
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        menu.setOnClickListener(){
            drawerOpen()
        }

        search.setOnClickListener(){
            tp_toolbarSearch.visibility= View.VISIBLE
        }
        back_toolbar.setOnClickListener(){
            ChatAlladapter!!.filter!!.filter("")
            tp_toolbarSearch.visibility= View.GONE
        }

        et_search!!.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(cs: CharSequence, arg1: Int, arg2: Int, arg3: Int) {
                ChatAlladapter!!.filter!!.filter(cs)
            }

            override fun beforeTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int) {
            }

            override fun afterTextChanged(arg0: Editable) {
                //ChatAlladapter!!.filter!!.filter(arg0)
            }
        })
    }
    fun drawerOpen() {
      //  val drawer: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            drawer_layout.openDrawer(GravityCompat.START)
        }
    }

    override fun onPause() {
        ChatAlladapter!!.filter!!.filter("")
        tp_toolbarSearch.visibility= View.GONE
        super.onPause()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId


        if (id == R.id.Share) {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(
                Intent.EXTRA_TEXT,
                "Download this Application from https://play.google.com/store/apps/details?id=$packageName"
            )
            startActivity(Intent.createChooser(intent, "Share"))
        } else if (id == R.id.Rateus) {
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=$packageName")
                    )
                )
            } catch (anfe: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/details?id=$packageName")
                    )
                )
            }
        }
       // val drawer: DrawerLayout = findViewById(R.id.drawer_layout)
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }


}
