package com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.activities

import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.BuildConfig
import com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.R
import kotlinx.android.synthetic.main.chat_design_one.*
import kotlinx.android.synthetic.main.full_screen.*
import saveit.whatsappstatussaver.whatsappsaver.Utils.CommonUtils
import saveit.whatsappstatussaver.whatsappsaver.Utils.CommonUtils.Companion.tabPosition
import java.io.File


class FullScreen : AppCompatActivity() {
    private var mediaPlayer: MediaController? = null
    var builder: AlertDialog.Builder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.mediumseagreen))
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.mediumseagreen))
        }
        val mActionBar = supportActionBar
        mActionBar!!.title="Whats Tools"
        mActionBar.setDisplayHomeAsUpEnabled(true)
        val colorDrawable = ColorDrawable(Color.parseColor("#17C17E"))
        mActionBar.setBackgroundDrawable(colorDrawable)      // Set BackgroundDrawable
        mActionBar.elevation = 0f   //remove divider line
        super.onCreate(savedInstanceState)
        setContentView(R.layout.full_screen)

        if (tabPosition == 2) {
            cv_delete.visibility = View.GONE
            cv_save.visibility = View.VISIBLE
        } else if (tabPosition == 3) {
            cv_save.visibility = View.GONE
            cv_delete.visibility = View.VISIBLE
        } else if (tabPosition == 1) {
            cv_delete.visibility = View.GONE
            cv_save.visibility = View.VISIBLE
        }

        // val iv_fullscreen  = findViewById<ImageView>(R.id.iv_fullscreen)
        val vv_fullscreen_video = findViewById<VideoView>(R.id.vv_fullscreen_video)

        val file = intent.getSerializableExtra("FullImage") as File
        val Positionfile = intent.getSerializableExtra("positionOfFile") as Int

        if (file.exists()) {
            if (file.name.endsWith(".png") || file.name.endsWith(".jpg")) {
                iv_fullscreen.visibility = View.VISIBLE
                vv_fullscreen_video.visibility = View.GONE
                val myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath())
                iv_fullscreen.setImageBitmap(myBitmap)
            } else {
                iv_fullscreen.visibility = View.GONE
                vv_fullscreen_video.visibility = View.VISIBLE
                val videoToPlay = Uri.parse(file.toString())
                playVid(videoToPlay)
            }
            cv_share.setOnClickListener() {
                val uri = FileProvider.getUriForFile(
                    cv_share.context,
                    BuildConfig.APPLICATION_ID + ".provider",
                    file
                )
                CommonUtils.Share(cv_share.context, uri)
            }
            cv_save.setOnClickListener() {
                val isSavede = CommonUtils.SaveFile(
                    file, cv_save.context,
                    CommonUtils.DIRECTORY_TO_SAVE_MEDIA_NOW
                )
                if (isSavede == true) {
                    Toast.makeText(this, "Saved Successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        this,
                        "Failed to Saved, Check Storage Permission!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            cv_delete.setOnClickListener() {
                builder = AlertDialog.Builder(this)
                builder!!.setMessage("Are you sure you want to delete this").setTitle("Delete ")
                builder!!.setMessage("Do you want to close this application ?")
                    .setCancelable(false)
                    .setPositiveButton(
                        "Yes"
                    ) { dialog, id ->
                        if (tabPosition == 3) {
                            if (file.name.endsWith(".png") || file.name.endsWith(".jpg")) {
                                CommonUtils.rvImagesAdapterD!!.RemoveData(Positionfile)
                            } else if (file.name.endsWith(".mp4") || file.name.endsWith(".gif")) {
                                CommonUtils.rvVideosAdapterD!!.RemoveData(Positionfile)
                            }
                        }
                        finish()
                    }
                    .setNegativeButton(
                        "No"
                    ) { dialog, id -> //  Action for 'NO' Button
                        dialog.cancel()
                        Toast.makeText(
                            applicationContext, "you choose no action for alertbox",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                val alert: AlertDialog = builder!!.create()
                alert.setTitle("AlertDialogExample")
                alert.show()
            }

        }
    }

    private fun playVid(u: Uri?) {
        mediaPlayer = MediaController(this)
        vv_fullscreen_video.setMediaController(mediaPlayer)
        vv_fullscreen_video.start()
        vv_fullscreen_video.setVideoURI(u)
    }
      override fun onSupportNavigateUp(): Boolean {
          onBackPressed()
          return true
      }


}