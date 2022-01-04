package com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import java.util.*
import androidx.recyclerview.widget.RecyclerView
import com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.R
import com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.models.SectionModel
import com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.utils.FileModel
import com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.utils.GridSpacingItemDecoration
import kotlinx.android.synthetic.main.section_custom_row_layout.view.*
import java.io.Serializable

class SectionRecyclerViewAdapter(
    private val context_: Context?,
    private val items: ArrayList<SectionModel>
) : RecyclerView.Adapter<SectionViewHolder>(), Serializable {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): SectionViewHolder {
        val mainGroup = LayoutInflater.from(viewGroup.context).inflate(R.layout.section_custom_row_layout, viewGroup, false) as ViewGroup
        // val view = LayoutInflater.from(context_).inflate(R.layout.section_custom_row_layout, viewGroup, false)
        return SectionViewHolder(mainGroup)

    }
    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {
        val sectionModel = items[position]
        holder.sectionLabel.text = sectionModel.sectionLabel
        holder.itemRecyclerView.setHasFixedSize(true)
        holder.itemRecyclerView.isNestedScrollingEnabled = false

        val spanCount = 3 // 2 columns
        val spacing = 9 // 20px
        val includeEdge = false

        holder.itemRecyclerView!!.addItemDecoration(
            GridSpacingItemDecoration(
                spanCount,
                spacing,
                includeEdge
            )
        )

        val mLinearLayoutManager = GridLayoutManager(context_, 3)
        holder.itemRecyclerView.layoutManager = mLinearLayoutManager
        val mLinearLayoutManager2 = GridLayoutManager(context_, 3)
        holder.itemRecyclerView.layoutManager = mLinearLayoutManager2

        val adapter = ItemRecyclerViewAdapter(context_!!, sectionModel.itemArrayList)
        holder.itemRecyclerView.adapter = adapter
    }


    interface OnClickListener {
        fun OnDeleteItem(position: Int)
        fun OnItemClick(view: View, obj: FileModel, pos: Int)
        fun OnItemLongClick(view: View, obj: FileModel, pos: Int)
    }


}

class SectionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var sectionLabel = view.section_label
    //var showAllButton = view.section_show_all_button
    var itemRecyclerView = view.item_recycler_view

}
