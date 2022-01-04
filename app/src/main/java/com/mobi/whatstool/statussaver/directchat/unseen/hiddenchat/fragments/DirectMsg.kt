package com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.hbb20.CountryCodePicker


class DirectMsg : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView=  inflater.inflate(R.layout.tab5_direct_msg, container, false)

        val msg  = rootView!!.findViewById<EditText>(R.id.msg_edittext)
        val phone  = rootView.findViewById<EditText>(R.id.number_edittext)
        val direct_messge_butn  = rootView.findViewById<Button>(R.id.direct_messge_butn)
        val countryCodePicker  = rootView.findViewById<CountryCodePicker>(R.id.number_code)
        countryCodePicker!!.registerCarrierNumberEditText(phone)

        direct_messge_butn.setOnClickListener { v: View? ->
            val _msg = msg.text
            if (_msg.length > 0) {
                val number: String = countryCodePicker.getFullNumberWithPlus()
                if (number.trim { it <= ' ' }
                        .isEmpty() || !countryCodePicker.isValidFullNumber()
                ) {
                    Toast.makeText(
                        activity,
                        "Please Enter Correct Number",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val cNumb = number.replace("+", " ")
                    try {
                        openWhatsAppConversationUsingUri(rootView.context, cNumb, _msg.toString())
                    } catch (e: Exception) {
                        Toast.makeText(
                            activity,
                            "WhatsApp cannot be opened", Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        return rootView
    }


    fun openWhatsAppConversationUsingUri(context: Context, numberWithCountryCode: String, message: String) {
        val uri =
            Uri.parse("https://api.whatsapp.com/send?phone=$numberWithCountryCode&text=$message")
        val sendIntent = Intent(Intent.ACTION_VIEW, uri)
        context.startActivity(sendIntent)
    }

}// Req