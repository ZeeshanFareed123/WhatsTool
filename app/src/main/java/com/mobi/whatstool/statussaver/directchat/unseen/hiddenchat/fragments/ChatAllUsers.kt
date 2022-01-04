package com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat

import android.content.*
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.db.Student
import com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.services.NotificationListenerService
import com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.viewModel.AllViewModel
import org.koin.android.ext.android.inject
import androidx.lifecycle.Observer
import com.amitshekhar.DebugDB
import com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.activities.ChatOneUser
import com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.adapter.AdapterChatAllUser
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import saveit.whatsappstatussaver.whatsappsaver.Utils.CommonUtils.Companion.ChatAlladapter
import saveit.whatsappstatussaver.whatsappsaver.Utils.CommonUtils.Companion.isRead

class ChatAllUsers : Fragment() {
    var _nam:String?=null
    private val viewModel: AllViewModel by inject()
    private val students: ArrayList<Student> = ArrayList()

    private var interceptedNotificationImageView: ImageView? = null
    private var imageChangeBroadcastReceiver: ImageChangeBroadcastReceiver? = null

    private var mInterstitialAd: InterstitialAd? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.tab1_chat, container, false)

        mInterstitialAd =  InterstitialAd(activity);
        mInterstitialAd!!.setAdUnitId(resources.getString(R.string.interstitial_full_screen))
        mInterstitialAd!!.loadAd(AdRequest.Builder().build())

        mInterstitialAd!!.adListener = object: AdListener() {
            override fun onAdClosed() {
                val intent = Intent(getActivity(), ChatOneUser::class.java)
                intent.putExtra("name", _nam)
                getActivity()!!.startActivity(intent)
            }
        }

        val chat_recyclerView = rootView.findViewById<RecyclerView>(R.id.chat_recyclerView)
       // val et_search = rootView.findViewById<EditText>(R.id.et_search)
        val mLinearLayoutManager = GridLayoutManager(context, 1)
        chat_recyclerView!!.layoutManager = mLinearLayoutManager

        // Finally we register a receiver to tell the MainActivity when a notification has been received
        imageChangeBroadcastReceiver = ImageChangeBroadcastReceiver()
        val intentFilter = IntentFilter()
        intentFilter.addAction("com.github.chagall.notificationlistenerexample")
        LocalBroadcastManager.getInstance(context!!).registerReceiver(
            imageChangeBroadcastReceiver!!, IntentFilter()
        )



        viewModel.lastMsgOfAllUsers.observe(this, Observer {
            students.clear()
            students.addAll(it!!)
            Log.i("aaaaNAMEaa", students.toString())
            val ip = DebugDB.getAddressLog()
            Log.i("aaaaIP", ip.toString())
            val reversedStdList = students.reversed()
            ChatAlladapter = AdapterChatAllUser(reversedStdList,object : AdapterChatAllUser.OnClickListener {
                override fun OnDeleteItem(position: Int) {
                    TODO("Not yet implemented")
                }
                override fun OnItemClick(view: View, name: String, pos: Int) {
                    _nam= name
                    if (mInterstitialAd!!.isLoaded()) {
                        mInterstitialAd!!.show();
                    } else {
                        val intent = Intent(getActivity(), ChatOneUser::class.java)
                        intent.putExtra("name", name)
                        getActivity()!!.startActivity(intent)
                    }

                }
                override fun OnItemLongClick(view: View, pos: Int) {
                    TODO("Not yet implemented")
                }
            })
            chat_recyclerView.adapter = ChatAlladapter
            ChatAlladapter!!.notifyDataSetChanged()
        })
        return rootView
    }

    override fun onResume() {
        super.onResume()
        isRead = false
    }

    /**
     * Image Change Broadcast Receiver.
     * We use this Broadcast Receiver to notify the Main Activity when
     * a new notification has arrived, so it can properly change the
     * notification image
     */
    inner class ImageChangeBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val receivedNotificationCode = intent.getIntExtra("Notification Code", -1)
            changeInterceptedNotificationImage(receivedNotificationCode)
        }
    }

    /**
     * Change Intercepted Notification Image
     * Changes the MainActivity image based on which notification was intercepted
     * @param notificationCode The intercepted notification code
     */
    private fun changeInterceptedNotificationImage(notificationCode: Int) {
        when (notificationCode) {
            NotificationListenerService.InterceptedNotificationCode.WHATSAPP_CODE -> interceptedNotificationImageView!!.setImageResource(
                Log.i("iaminn.....", "Name =testing")
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(context!!)
            .unregisterReceiver(imageChangeBroadcastReceiver!!)
    }

}