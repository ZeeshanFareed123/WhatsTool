package com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.R
import com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.utils.TinyDB
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.desclaimer.*


private const val SPLASH_TIME = 3000L

class Splash: AppCompatActivity() {

    private var isResumed = false
    private val runAfter = Runnable {
        if (isResumed ) { gotoNextActivity()
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        if((TinyDB.getInstance(this).getBoolean("checkedDesclaimer")) ){
            if((TinyDB.getInstance(this).getBoolean("checkedPermission"))){
                setContentView(R.layout.splash)
                //this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
                getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.mediumseagreen))
                getSupportActionBar()!!.hide()

                MobileAds.initialize(
                    this
                ) { }
            }else{
                val i = Intent(this, IntroScreen::class.java)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(i)
                finish()
            }

        }else{
            setContentView(R.layout.desclaimer)
          //  this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
            getSupportActionBar()!!.hide()
            ok_cont.setOnClickListener(){
                contin()
            }
            cancell.setOnClickListener(){
                finish()
            }
        }

    }
    private fun gotoNextActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun contin() {
        TinyDB.getInstance(this).putBoolean("checkedDesclaimer", true)
        val i = Intent(this, IntroScreen::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

    override fun onResume() {
        super.onResume()
        if((TinyDB.getInstance(this).getBoolean("checkedPermission"))){
            isResumed = true
            Handler().postDelayed(runAfter, SPLASH_TIME)
        }

    }

    override fun onPause() {
        super.onPause()
        if((TinyDB.getInstance(this).getBoolean("checkedPermission"))) {
            isResumed = false
        }
    }
}