package com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.activities

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.R
import com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.utils.TinyDB
import kotlinx.android.synthetic.main.permission.*


class IntroScreen : AppCompatActivity() {
    private val ACTION_NOTIFICATION_LISTENER_SETTINGS =
        "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.permission)

        Next.setOnClickListener() {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ) {
                if(isNLServiceRunning()){
                    TinyDB.getInstance(this).putBoolean("checkedPermission", true)
                    val i = Intent(this, MainActivity::class.java)
                    i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(i)
                    finish()
                }else{
                    Toast.makeText(applicationContext, "Notification Permission Required", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(applicationContext, "Storage Permission Required", Toast.LENGTH_SHORT).show()
            }
        }

        storage.setOnClickListener() {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(
                    applicationContext,
                    "Storage Permission Already taken",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                requestStoragePermission()
            }

        }

        Notification.setOnClickListener() {
            if (!isNLServiceRunning()) {
                startActivity( Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS));
               /* val intent =
                    Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
                startActivityForResult(intent, 50)*/
            } else {
                Toast.makeText(
                    applicationContext,
                    "Notification Permission Already taken",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }
    private fun isNLServiceRunning(): Boolean {
        val pkgName = packageName
        val flat: String = Settings.Secure.getString(
            contentResolver,
            "enabled_notification_listeners"
        )
        if (!TextUtils.isEmpty(flat)) {
            val names = flat.split(":".toRegex()).toTypedArray()
            for (i in names.indices) {
                val cn = ComponentName.unflattenFromString(names[i])
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.packageName)) {
                        return true
                    }
                }
            }
        }
        return false
    }

/*    private fun isNLServiceRunning(): Boolean {
        val manager =
            getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (NotificationListenerService::class.java.getName() == service.service.className) {
                return true
            }
        }
        return false
    }*/


 /*   override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
          if (requestCode == 50) {
                  if (isNLServiceRunning()) {
                      Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show()
                  } else { Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show()
                  }
          }
    }*/
    private fun requestStoragePermission(): Boolean {
        return if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1
            )
            false
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1
            )
            true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
             if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                  Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show()
             } else { Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show()
             }
        }
    }

}