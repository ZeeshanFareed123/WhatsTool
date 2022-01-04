package com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.R
import com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.db.Student

class AdapterChatOneUser(val userList: List<Student>)
    : RecyclerView.Adapter<ViewHolder_ChatOneUser>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder_ChatOneUser {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.chat_design_one_adapter, parent, false)
        return ViewHolder_ChatOneUser(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder_ChatOneUser, position: Int) {
        holder.bindItems(userList[position])
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }

}

class ViewHolder_ChatOneUser(view: View) : RecyclerView.ViewHolder(view) {
    val tv_chat_one = view.findViewById(R.id.tv_all_msg) as TextView
    val tv_time_date = view.findViewById(R.id.tv_time_date) as TextView

    fun bindItems(user: Student) {
        tv_chat_one.text = user.message
        tv_time_date.text = user.time
    }
}