package com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.adapter

import android.content.Context
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import java.util.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.BuildConfig.APPLICATION_ID
import com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.R
import com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.utils.FileModel
import kotlinx.android.synthetic.main.list_img.view.*
import kotlinx.android.synthetic.main.list_vedio.view.*
import saveit.whatsappstatussaver.whatsappsaver.Utils.CommonUtils
import java.io.File
import java.io.Serializable

class Status(
    private var items: ArrayList<FileModel>,
    val checkForAdapter: Int,
    var onClickListener: OnClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Serializable {

    private val selected_items: SparseBooleanArray = SparseBooleanArray()

    fun getAllImageList(): ArrayList<FileModel> {
        return items
    }

    private var context_: Context? = null


    override fun getItemCount(): Int {
        return items.size
    }

    fun GetFileToShare(selectedItemPositions: Int): File {
        val file=items.get(selectedItemPositions).name!!
        return file
    }

    override fun getItemViewType(position: Int): Int {
        if (checkForAdapter == FileModel.Video) {
            return FileModel.Video
        } else {
            return FileModel.Images
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context_ = viewGroup.context
        if (viewType == FileModel.Images) {
            val mainGroup = LayoutInflater.from(viewGroup.context).inflate(R.layout.list_img, viewGroup, false) as ViewGroup
            return ImagesViewHolder(mainGroup)
        } else {
            val mainGroup = LayoutInflater.from(viewGroup.context).inflate(R.layout.list_vedio, viewGroup, false) as ViewGroup
            return VideoViewHolder(mainGroup)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ImagesViewHolder) {
            holder.bind(items.get(position))
            holder.Img_list.isActivated = selected_items.get(position, false)
            holder.Img_list.setOnClickListener{
                onClickListener.OnItemClick(it, items.get(position), position) }

            holder.Img_list.setOnLongClickListener(object : View.OnLongClickListener {
                override fun onLongClick(p0: View?): Boolean {
                    onClickListener.OnItemLongClick(p0!!, items.get(position), position)
                    return true }
            })
           ToggleCheckedIconImage(holder, position)

        } else if (holder is VideoViewHolder) {
            holder.bind(items.get(position))
            holder.vid_list.isActivated = selected_items.get(position, false)
            holder.vid_list.setOnClickListener{
                onClickListener.OnItemClick(it, items.get(position), position) }
            holder.vid_list.setOnLongClickListener(object : View.OnLongClickListener {
                override fun onLongClick(p0: View?): Boolean {
                    onClickListener.OnItemLongClick(p0!!, items.get(position), position)
                    return true }
            })
         ToggleCheckedIconVideo(holder, position)
        }
    }

    private fun ToggleCheckedIconImage(holder: RecyclerView.ViewHolder, position: Int) {
        if (selected_items.get(position, false)) {
            (holder as ImagesViewHolder).ic_icon_tick2.visibility = View.VISIBLE
            (holder as ImagesViewHolder).ic_icon_tick222.visibility = View.VISIBLE

        } else {
            (holder as ImagesViewHolder).ic_icon_tick2.visibility = View.GONE
            (holder as ImagesViewHolder).ic_icon_tick222.visibility = View.GONE
        }
    }
    private fun ToggleCheckedIconVideo(holder: RecyclerView.ViewHolder, position: Int) {
        if (selected_items.get(position, false)) {
            (holder as VideoViewHolder).ic_icon_tick2_vid.visibility = View.VISIBLE
            (holder as VideoViewHolder).ic_icon_tick2_vid22.visibility = View.VISIBLE

        } else {
            (holder as VideoViewHolder).ic_icon_tick2_vid.visibility = View.GONE
            (holder as VideoViewHolder).ic_icon_tick2_vid22.visibility = View.GONE
        }
    }

    fun RemoveData(position: Int) {
        val fileName=items.get(position).name
        items.get(position).name!!.delete()
        items.removeAt(position)
        notifyDataSetChanged()
        //*****************Refreshing the Gallery after Deleting Image/Video ********************///
        Log.i("test", " aaaaa" +fileName )
        MediaScannerConnection.scanFile(context_, arrayOf(fileName.toString()), null) { path, uri ->
            Log.v("grokkingandroid", "file $path was scanned seccessfully: $uri") }
        //***************************************************************************************///
    }
    fun ClearSelections() {
        selected_items.clear()
        notifyDataSetChanged()
    }
    fun GetSelectedItems(): List<Int> {
        val items :ArrayList<Int> = ArrayList(selected_items.size())
        for (i in 0 until selected_items.size()) {
            items.add(selected_items.keyAt(i))
        }
        return items }

    fun GetSelectedItemCount(): Int {
        return selected_items.size()
    }

    fun ToggleSelection(pos: Int) {
        //current_selected_idx = pos
        if (selected_items.get(pos, false)) {
            selected_items.delete(pos)
        } else {
            selected_items.put(pos, true) }
        notifyItemChanged(pos)
    }

    private fun Delete_file(position: Int) {
        onClickListener.OnDeleteItem(position)
    }

    fun GetItem(): Int {
        return items.size
    }

    fun SaveData(position: Int, contxt: Context?):Boolean {
        val chekSave= CommonUtils.SaveFile(
            items.get(position).name!!,
            contxt,
            CommonUtils.DIRECTORY_TO_SAVE_MEDIA_NOW
        )
        return chekSave==true }

    fun ShareData(position: List<Int>) {
        val imageUriArray = ArrayList<Uri>()
        for(i in 0 until position.size){
            val pos=position[i]
            val dataToShare=items.get(pos).name!!
            val uri1 = FileProvider.getUriForFile(context_!!, APPLICATION_ID + ".provider", dataToShare)
            imageUriArray.add(uri1) }
        val shareIntent = Intent(Intent.ACTION_SEND_MULTIPLE)
        shareIntent.type = "application/octet-stream"
        shareIntent.type = "*/*"
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUriArray)
        context_!!.startActivity(Intent.createChooser(shareIntent, "send"))


    }

    interface OnClickListener {
        fun OnDeleteItem(position: Int)
        fun OnItemClick(view: View, obj: FileModel, pos: Int)
        fun OnItemLongClick(view: View, obj: FileModel, pos: Int)
    }
}

@Suppress("DEPRECATION")
class ImagesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var Img_list = view.iv_main_photo
    var ic_icon_tick2 = view.ic_icon_tick2
    var ic_icon_tick222 = view.ic_icon_tick222

    fun bind(fileModel: FileModel) {
       /* Glide.with(Img_list.context)
            .downloadOnly()
            .diskCacheStrategy(DiskCacheStrategy.DATA) // Cache resource before it's decoded
            .load(fileModel.name!!.absoluteFile)
            .into(Img_list)*/
        Glide.with(Img_list.context).load(fileModel.name!!.absoluteFile).into(Img_list)
    }
}

class VideoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var vid_list = view.iv_main_vid
    var ic_icon_tick2_vid = view.ic_icon_tick2_vid
    var ic_icon_tick2_vid22 = view.ic_icon_tick2_vid22
    fun bind(fileModel: FileModel) {
            Glide.with(vid_list.context).load(fileModel.name!!.absoluteFile).into(vid_list)
    }
}
