package com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.activities

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.R
import com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.adapter.AdapterChatOneUser
import com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.db.Student
import com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.viewModel.AllViewModel
import kotlinx.android.synthetic.main.chat_design_one.*
import org.koin.android.ext.android.inject
import saveit.whatsappstatussaver.whatsappsaver.Utils.CommonUtils.Companion.isRead
import saveit.whatsappstatussaver.whatsappsaver.Utils.CommonUtils.Companion.oneUserName
import saveit.whatsappstatussaver.whatsappsaver.Utils.CommonUtils.Companion.showDeleteDialog


class ChatOneUser : AppCompatActivity() {

    val viewModel: AllViewModel by inject()

    private val students: ArrayList<Student> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat_design_one)

        isRead = true
        val chat_recyclerView = findViewById<RecyclerView>(R.id.chat_one_recyclerView)
        val mLinearLayoutManager = GridLayoutManager(this, 1)
        mLinearLayoutManager.setReverseLayout(true)
        chat_recyclerView!!.layoutManager = mLinearLayoutManager

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.mediumseagreen))
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.mediumseagreen))
        }

        direct_messge_butn.setOnClickListener(){
        val launchIntent =
                packageManager.getLaunchIntentForPackage("com.whatsapp")
            startActivity(launchIntent)
        }

        var bundle: Bundle? = intent.extras
        oneUserName = bundle!!.getString("name").toString()

        viewModel.updateCounterToZero(oneUserName)


        val mActionBar = supportActionBar
        mActionBar!!.title = oneUserName

        mActionBar.setHomeAsUpIndicator(R.drawable.ic_back)
        mActionBar.setDisplayHomeAsUpEnabled(true)
        val colorDrawable = ColorDrawable(Color.parseColor("#17C17E"))
        mActionBar.setBackgroundDrawable(colorDrawable)      // Set BackgroundDrawable
        mActionBar.elevation = 0f //remove divider line

        viewModel.allMsgsOfOneUsers().observe(this, Observer {
            students.clear()
            students.addAll(it!!)
            val reversedStdList = students.reversed()
            Log.i("sasdasdasd", " Already Exist with PreviousMsg ${reversedStdList}")
            val adapter = AdapterChatOneUser(reversedStdList)
            chat_recyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
        })
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.delete, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.delete -> {
                showDeleteDialog(this)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}
