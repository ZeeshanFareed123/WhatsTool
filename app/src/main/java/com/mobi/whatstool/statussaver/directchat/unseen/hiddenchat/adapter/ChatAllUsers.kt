package com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.R
import com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.db.Student
import java.text.SimpleDateFormat
import java.util.*
class AdapterChatAllUser(var userList: List<Student>, var onClickListener: OnClickListener)
    : RecyclerView.Adapter<ViewHolder_ChatAllUser>(), Filterable {

    private var filteredNameList: List<Student>? = null
    init {
        filteredNameList = userList
    }
    var searchableList: MutableList<Student> = arrayListOf()


    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder_ChatAllUser {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.tab1_chat_adapter, parent, false)
        return ViewHolder_ChatAllUser(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder_ChatAllUser, position: Int) {
        holder.bindItems(filteredNameList!![position])
        holder.cv_img_list1.setOnClickListener(){
            onClickListener.OnItemClick(it, filteredNameList!!.get(position).name, position)
        }
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return filteredNameList!!.size
    }

    interface OnClickListener {
        fun OnDeleteItem(position: Int)
        fun OnItemClick(
            it: View,
            name: String,
            position: Int
        )
        fun OnItemLongClick(view: View, pos: Int) }

    override fun getFilter(): Filter? {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    filteredNameList= userList
                } else {
                    val resultList = ArrayList<Student>()
                    for (row in filteredNameList!!) {
                        if (row.name.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))) {
                            resultList.add(row)
                        }
                    }
                    filteredNameList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = filteredNameList
                return filterResults
            }

            override fun publishResults(
                constraint: CharSequence,
                results: FilterResults
            ) {
                filteredNameList = (results.values as List<Student>?)!!
                Log.i("lstMsgggg", " filteredNameList: ${filteredNameList}")
                Log.i("lstMsgggg", " UserList:  ${userList}")
                notifyDataSetChanged()
            }
        }
    }

}

class ViewHolder_ChatAllUser(view: View) : RecyclerView.ViewHolder(view) {

    val textViewName = view.findViewById(R.id.textViewUsername) as TextView
    val cv_img_list1 = view.findViewById(R.id.cv_img_list) as CardView
    val textViewAddress = view.findViewById(R.id.textViewAddress) as TextView
    val textViewTime = view.findViewById(R.id.textViewTime) as TextView
    val tvNotReaded = view.findViewById(R.id.tvNotReaded) as TextView

    fun bindItems(user: Student) {
        textViewName.text = user.name
        textViewAddress.text = user.message
       // tvNotReaded.text = user.countMsg
        val checkCount = user.countMsg
        Log.i("lstMsgggg", "${user.name} Already Exist with PreviousMsg ${checkCount}")
        if(Integer.parseInt(checkCount)==0 ){
            tvNotReaded.setVisibility(View.INVISIBLE)
        }
        else{
            tvNotReaded.setVisibility(View.VISIBLE)
            tvNotReaded.text = user.countMsg
        }

        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        val currentDate:String = dateFormat.format(Date()).toString()
        val previousDate = user.time
        val _previousDate = previousDate.substring(0, 10)

        if(_previousDate==currentDate){
            val res = previousDate.substring(11, 19)
            textViewTime.text = res
        }else{
            val res = _previousDate
            textViewTime.text = res
        }
    }

}