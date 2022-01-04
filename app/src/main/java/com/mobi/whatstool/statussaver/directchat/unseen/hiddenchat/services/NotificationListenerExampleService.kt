package com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.services

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.os.Environment
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.annotation.RequiresApi
import com.github.phajduk.rxfileobserver.FileEvent
import com.github.phajduk.rxfileobserver.RxFileObserver
import com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.db.Student
import com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.services.NotificationListenerService.ApplicationPackageNames.WHATSAPP_PACK_NAME
import com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.viewModel.AllViewModel
import org.koin.android.ext.android.inject
import rx.Observable
import saveit.whatsappstatussaver.whatsappsaver.Utils.CommonUtils
import saveit.whatsappstatussaver.whatsappsaver.Utils.CommonUtils.Companion.PathToStoreInDB
import saveit.whatsappstatussaver.whatsappsaver.Utils.CommonUtils.Companion.Pre_DELETED_DIRECTORY
import saveit.whatsappstatussaver.whatsappsaver.Utils.CommonUtils.Companion.basePath
import saveit.whatsappstatussaver.whatsappsaver.Utils.CommonUtils.Companion.count
import saveit.whatsappstatussaver.whatsappsaver.Utils.CommonUtils.Companion.isAudio
import saveit.whatsappstatussaver.whatsappsaver.Utils.CommonUtils.Companion.isPhoto
import saveit.whatsappstatussaver.whatsappsaver.Utils.CommonUtils.Companion.isVideo
import saveit.whatsappstatussaver.whatsappsaver.Utils.CommonUtils.Companion.path2Watch
import saveit.whatsappstatussaver.whatsappsaver.Utils.CommonUtils.Companion.pathForAudios
import saveit.whatsappstatussaver.whatsappsaver.Utils.CommonUtils.Companion.pathForImages
import saveit.whatsappstatussaver.whatsappsaver.Utils.CommonUtils.Companion.pathForVideos
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
class NotificationListenerService : NotificationListenerService() {

    private var observPictures2: moblgx_FileObsrvr? = null
    var context: Context = this
    private val mViewModel: AllViewModel by inject()

    private object ApplicationPackageNames {
        val FACEBOOK_PACK_NAME = "com.facebook.katana"
        val FACEBOOK_MESSENGER_PACK_NAME = "com.facebook.orca"
        val WHATSAPP_PACK_NAME = "com.whatsapp"
        val INSTAGRAM_PACK_NAME = "com.instagram.android"
    }

    object InterceptedNotificationCode {
        const val FACEBOOK_CODE = 1
        const val WHATSAPP_CODE = 2
        const val INSTAGRAM_CODE = 3
        const val OTHER_NOTIFICATIONS_CODE = 4 // We ignore all notification with code == 4
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        matchNotificationCode(sbn)
        /*if (notificationCode != InterceptedNotificationCode.OTHER_NOTIFICATIONS_CODE) {
            val intent = Intent("com.github.chagall.notificationlistenerexample")
            intent.putExtra("Notification Code", notificationCode)
           sendBroadcast(intent)
        }*/
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        /* val notificationCode = matchNotificationCode(sbn)
         if (notificationCode != InterceptedNotificationCode.OTHER_NOTIFICATIONS_CODE) {
             val activeNotifications = this.activeNotifications
             if (activeNotifications != null && activeNotifications.size > 0) {
                 for (i in activeNotifications.indices) {
                     if (notificationCode == matchNotificationCode(activeNotifications[i])) {
                         val intent = Intent("com.github.chagall.notificationlistenerexample")
                         intent.putExtra("Notification Code", notificationCode)
                       //  LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
                         sendBroadcast(intent)
                         break
                     }
                 }
             }
         }*/
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    private fun matchNotificationCode(statusBarNotification: StatusBarNotification) {
        val packageName = statusBarNotification.packageName
        if (packageName == WHATSAPP_PACK_NAME) {
            val title: String?
            val msg: String?
            val Msgcheck1 = "Checking for new messages"
            val Msgcheck2 = "messages from"
            val Msgcheck3 = "new messages"
            val Msgcheck4 = "This message was deleted"

            val bundle = statusBarNotification.notification.extras
            //val charSequenceArray = bundle.getCharSequenceArray("android.textLines")
            title = bundle.getString("android.title")
            msg = " " + bundle.getCharSequence("android.text")!!.toString()

            /*****set path for observer where whatsapp save file
            these will execute only if msg is photo/video/audio******/
            if(msg.contains(isPhoto)){
                path2Watch = basePath+pathForImages
                Log.i("fileoberveraaa: ", "isImage")
            }
            else if(msg.contains(isVideo)){
                path2Watch = basePath+ pathForVideos
                Log.i("fileoberveraaa: ", "isVideo")
            }
            else if (msg.contains(isAudio)){
                path2Watch = basePath+ pathForAudios
            }

            //file observer
            val sdCardFileEvents: Observable<FileEvent>? = RxFileObserver.create(path2Watch)
            sdCardFileEvents!!.subscribe { fileEvent ->
                if(fileEvent.isMovedTo){
                    PathToStoreInDB = path2Watch+"/"+fileEvent.path  //this is the path of file with name, it will store in DB
                    Log.i("fileoberveraaaaaaaaaa:", "isMovedTo = $PathToStoreInDB")
                    val file = File(PathToStoreInDB)  //convert string to file to save media
                    val saveRes = CommonUtils.SaveFile(file, this, CommonUtils.Pre_DELETED_DIRECTORY)
                }
                if (fileEvent.isDelete){
                    Log.i("fileoberveraaa", "isDelete = $PathToStoreInDB")
                    val file1InString = path2Watch+"/"+fileEvent.path  ///storage/emulated/0/WhatsApp/Media/WhatsApp Images/IMG-20200729-WA0038.jpg
                    val sdCard = Environment.getExternalStorageDirectory().toString() + Pre_DELETED_DIRECTORY  ///storage/emulated/0//WhatsappImp/
                    val dir = File(sdCard)
                    for (f in dir.listFiles()) {
                        if (f.isFile){
                            if(f.name==fileEvent.path){
                                val saveRes = CommonUtils.SaveFile(f, this, CommonUtils.Pos_DELETED_DIRECTORY)
                                Log.i("yessssssssss", "Found  $f")
                            }

                        }
                    }

                }
                if(fileEvent.isModify){
                    Log.i("fileoberveraaa", "isModify = ${fileEvent.path}")
                }
                if(fileEvent.isCreate){
                    Log.i("fileoberveraaa", "isCreate = ${fileEvent.path}")
                }
            }

            if (msg.contains(Msgcheck1, false) || msg.contains(Msgcheck2, false)
                || msg.contains(Msgcheck3, false) || msg.contains(Msgcheck4, false)
            ) {
                Log.i("iaminn", "Contain Substring = $msg")
            } else {
                val dateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm a")
                val currentTimeDate: String = dateFormat.format(Date()).toString()
                Log.i("iaminn", "Current Time = $currentTimeDate")

                mViewModel.insertStudent(
                    Student(
                        title.toString(),
                        msg.toString(), currentTimeDate, count.toString()
                    )
                )
            }
        } else {
            return
        }
    }

}
