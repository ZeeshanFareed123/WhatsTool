package com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.R
import com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.activities.ChatOneUser
import com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.activities.FullScreen
import java.io.File
import java.util.*

/**
 * Created by sonu on 24/07/17.
 */
class ItemRecyclerViewAdapter(
    private val context: Context,
    private val arrayList: ArrayList<File>
) : RecyclerView.Adapter<ItemRecyclerViewAdapter.ItemViewHolder>() {
    inner class ItemViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val img: ImageView
        val imgVid: ImageView

        init {
            img = itemView.findViewById<View>(R.id.iv_main_vid) as ImageView
            imgVid = itemView.findViewById<View>(R.id.iv_play_vid) as ImageView
        }
        fun bind(fileModel: File) {
            if(fileModel.name.endsWith(".mp4")){
                imgVid.visibility=View.VISIBLE
                Glide.with(img.context).load(fileModel.absoluteFile).into(img)
            }else{
                imgVid.visibility=View.INVISIBLE
                Glide.with(img.context).load(fileModel.absoluteFile).into(img)
            }
        }
        fun GetFileToShare(selectedItemPositions: Int): File {
            val file=arrayList.get(selectedItemPositions).absoluteFile
            return file
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_custom_row_layout, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(arrayList.get(position))
        holder.img.setOnClickListener {
            val intent = Intent(context, FullScreen::class.java)
            val file = holder.GetFileToShare(position)
            intent.putExtra("FullImage", file)
            intent.putExtra("positionOfFile", position)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

}
