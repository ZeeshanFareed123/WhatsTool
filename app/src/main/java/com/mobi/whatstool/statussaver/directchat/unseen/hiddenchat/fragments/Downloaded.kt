package com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.activities.FullScreen
import com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.adapter.Status
import com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.utils.FileModel
import com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.utils.GridSpacingItemDecoration
import com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.viewModel.AllViewModel
import kotlinx.android.synthetic.main.tab3_status.*
import kotlinx.android.synthetic.main.tab3_status.view.*
import org.koin.android.ext.android.inject
import saveit.whatsappstatussaver.whatsappsaver.Utils.CommonUtils
import saveit.whatsappstatussaver.whatsappsaver.Utils.CommonUtils.Companion.rvImagesAdapterD
import saveit.whatsappstatussaver.whatsappsaver.Utils.CommonUtils.Companion.rvVideosAdapterD


class Downloaded : Fragment() {
    private val viewModel: AllViewModel by inject()
    var cv_number_of_item: ConstraintLayout? = null
    var isImage: Boolean? = true


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.tab3_status, container, false)
        val cv_img = rootView!!.findViewById<CardView>(R.id.cv_img)
        val cv_vid = rootView.findViewById<CardView>(R.id.cv_vid)
        val tv_img = rootView.findViewById<TextView>(R.id.tv_img)
        val tv_vid = rootView.findViewById<TextView>(R.id.tv_vid)
        val tv_itm_select = rootView.findViewById<TextView>(R.id.tv_itm_select)
        val multu_cross = rootView.findViewById<ImageView>(R.id.multu_cross)
        val slect_all = rootView.findViewById<ImageView>(R.id.slect_all)
        val cv_delete = rootView.findViewById<CardView>(R.id.cv_delete_multpile)
        val cv_share = rootView.findViewById<CardView>(R.id.cv_share_multpile)
        val cv_save = rootView.findViewById<CardView>(R.id.cv_save_multpile)
        cv_number_of_item = rootView.findViewById<ConstraintLayout>(R.id.cv_item_selection)
        val rv_LiveStatusImage = rootView.findViewById<RecyclerView>(R.id.image_live_status)
        val rv_LiveStatusVideos = rootView.findViewById<RecyclerView>(R.id.videos_live_status)

        cv_delete.visibility=View.VISIBLE
        cv_save.visibility=View.GONE


        cv_number_of_item!!.setOnClickListener() {
            Toast.makeText(rootView.context, "asd", Toast.LENGTH_SHORT).show()
        }

        val spanCount = 3 // 2 columns
        val spacing = 9 // 20px
        val includeEdge = false
        rv_LiveStatusImage!!.addItemDecoration(
            GridSpacingItemDecoration(
                spanCount,
                spacing,
                includeEdge
            )
        )
        rv_LiveStatusVideos!!.addItemDecoration(
            GridSpacingItemDecoration(
                spanCount,
                spacing,
                includeEdge
            )
        )

        val mLinearLayoutManager = GridLayoutManager(rootView.context, 3)
        rv_LiveStatusImage.layoutManager = mLinearLayoutManager
        val mLinearLayoutManager2 = GridLayoutManager(rootView.context, 3)
        rv_LiveStatusVideos.layoutManager = mLinearLayoutManager2

        viewModel.getDownlodedImages(CommonUtils.DIRECTORY_TO_SAVE_MEDIA_NOW)

        viewModel.downloadedImage.observe(this, androidx.lifecycle.Observer {
            Log.i("eeeeee", "  lready Exist with PreviousMsg:  ${it}")
            CommonUtils.rvImagesAdapterD = Status(it, FileModel.Images, object : Status.OnClickListener {
                override fun OnDeleteItem(position: Int) {
                    TODO("Not yet implemented")
                }

                override fun OnItemClick(view: View, obj: FileModel, pos: Int) {
                    if (CommonUtils.rvImagesAdapterD?.GetSelectedItemCount()!! > 0) {
                        multiSelection(pos, CommonUtils.rvImagesAdapterD!!, rootView)
                    } else {
                        val fileImg = CommonUtils.rvImagesAdapterD?.GetFileToShare(pos)
                        val fullScreenIntent = Intent(rootView.context, FullScreen::class.java)
                        fullScreenIntent.putExtra("FullImage", fileImg)
                        fullScreenIntent.putExtra("positionOfFile", pos)
                        startActivity(fullScreenIntent)
                    }
                }
                override fun OnItemLongClick(view: View, obj: FileModel, pos: Int) {
                    multiSelection(pos, CommonUtils.rvImagesAdapterD!!, rootView)
                }
            })
            rv_LiveStatusImage.adapter = CommonUtils.rvImagesAdapterD
            CommonUtils.rvImagesAdapterD!!.notifyDataSetChanged()
        })

        viewModel.getDownlodedVideos(CommonUtils.DIRECTORY_TO_SAVE_MEDIA_NOW)
        viewModel.downloadedVideo.observe(this, androidx.lifecycle.Observer {
            Log.i("eeeeee", "  lready Exist with PreviousMsg:  ${it}")
            CommonUtils.rvVideosAdapterD = Status(it, FileModel.Video, object : Status.OnClickListener {
                override fun OnDeleteItem(position: Int) {
                    TODO("Not yet implemented")
                }

                override fun OnItemClick(view: View, obj: FileModel, pos: Int) {
                    if (CommonUtils.rvVideosAdapterD?.GetSelectedItemCount()!! > 0) {
                        multiSelection(pos, CommonUtils.rvVideosAdapterD!!, rootView)
                    } else {
                        val fileImg = CommonUtils.rvVideosAdapterD?.GetFileToShare(pos)
                        val fullScreenIntent = Intent(rootView.context, FullScreen::class.java)
                        fullScreenIntent.putExtra("FullImage", fileImg)
                        fullScreenIntent.putExtra("positionOfFile", pos)
                        startActivity(fullScreenIntent)
                    }
                }

                override fun OnItemLongClick(view: View, obj: FileModel, pos: Int) {
                    multiSelection(pos, CommonUtils.rvVideosAdapterD!!, rootView)
                }
            })
            rv_LiveStatusVideos.adapter = CommonUtils.rvVideosAdapterD
            CommonUtils.rvVideosAdapterD!!.notifyDataSetChanged()
        })

        multu_cross.setOnClickListener() {
            clearMultipleSlection()
            heightAndInvisibilty()
        }
        slect_all.setOnClickListener(){
            if (isImage!!) {
                val selectedImg = CommonUtils.rvImagesAdapterD?.GetSelectedItems()
                for (index in 0 until selectedImg!!.size) {
                    val position = selectedImg[index]
                    CommonUtils.rvImagesAdapterD!!.ToggleSelection(position)
                }
                val itemSize = CommonUtils.rvImagesAdapterD?.GetItem()
                for (i in 0 until itemSize!!) {
                    multiSelection(i, CommonUtils.rvImagesAdapterD!!, rootView)
                }
            } else {
                val selectedImg = CommonUtils.rvVideosAdapterD?.GetSelectedItems()
                for (index in 0 until selectedImg!!.size) {
                    val position = selectedImg[index]
                    CommonUtils.rvVideosAdapterD!!.ToggleSelection(position)
                }
                val itemSize = CommonUtils.rvVideosAdapterD?.GetItem()
                for (i in 0 until itemSize!!) {
                    multiSelection(i, CommonUtils.rvVideosAdapterD!!, rootView)
                }
            }
        }

        cv_img.setOnClickListener() {
            isImage = true
            CommonUtils.rvVideosAdapterD!!.ClearSelections()
            heightAndInvisibilty()
            rv_LiveStatusImage.setVisibility(View.VISIBLE)
            rv_LiveStatusVideos.setVisibility(View.INVISIBLE)
            cv_img.setCardBackgroundColor(
                ContextCompat.getColor(
                    rootView.context,
                    R.color.mediumseagreen
                )
            )
            cv_vid.setCardBackgroundColor(ContextCompat.getColor(rootView.context, R.color.white))
            tv_img.setTextColor(ContextCompat.getColor(rootView.context, R.color.white))
            tv_vid.setTextColor(ContextCompat.getColor(rootView.context, R.color.mediumseagreen))
        }
        cv_vid.setOnClickListener() {
            isImage = false
            CommonUtils.rvImagesAdapterD!!.ClearSelections()
            heightAndInvisibilty()
            rv_LiveStatusVideos.setVisibility(View.VISIBLE)
            rv_LiveStatusImage.setVisibility(View.INVISIBLE)
            cv_img.setCardBackgroundColor(ContextCompat.getColor(rootView.context, R.color.white))
            cv_vid.setCardBackgroundColor(
                ContextCompat.getColor(
                    rootView.context,
                    R.color.mediumseagreen
                )
            )
            tv_img.setTextColor(ContextCompat.getColor(rootView.context, R.color.mediumseagreen))
            tv_vid.setTextColor(ContextCompat.getColor(rootView.context, R.color.white))
        }
        cv_delete.setOnClickListener(){
            if (isImage!!) {
                CommonUtils.deleteMultipleItems( rootView.context, CommonUtils.rvImagesAdapterD!!)
            } else {
                CommonUtils.deleteMultipleItems( rootView.context,CommonUtils.rvVideosAdapterD)
            }
            clearMultipleSlection()
            heightAndInvisibilty()

        }
        cv_share.setOnClickListener() {
            if (isImage!!) {
                CommonUtils.shareMultipleItems(CommonUtils.rvImagesAdapterD)
            } else {
                CommonUtils.shareMultipleItems(CommonUtils.rvVideosAdapterD)
            }
        }
        return rootView
    }

    private fun heightAndVisibilty() {
        val count_No_Seletion = cv_number_of_item!!.getLayoutParams()
        count_No_Seletion.height = 80
        cv_number_of_item!!.setLayoutParams(count_No_Seletion)
        cv_number_of_item!!.visibility = View.VISIBLE

        val multple_ddelete_btn = multiple_del_btn!!.getLayoutParams()
        multple_ddelete_btn.height = 110
        multiple_del_btn!!.setLayoutParams(multple_ddelete_btn)
        multiple_del_btn!!.visibility = View.VISIBLE

    }
    private fun heightAndInvisibilty() {
        val count_No_Seletion = cv_number_of_item!!.getLayoutParams()
        count_No_Seletion.height = 0
        cv_number_of_item!!.setLayoutParams(count_No_Seletion)
        cv_number_of_item!!.visibility = View.INVISIBLE

        val multple_ddelete_btn = multiple_del_btn!!.getLayoutParams()
        multple_ddelete_btn.height = 0
        multiple_del_btn!!.setLayoutParams(multple_ddelete_btn)
        multiple_del_btn!!.visibility = View.INVISIBLE

    }


    private fun multiSelection(
        position: Int,
        rvAdapter: Status,
        rootView: View
    ) {
        heightAndVisibilty()
        rvAdapter.ToggleSelection(position)
        val count = rvAdapter.GetSelectedItemCount()
        if (count == 0) {
            heightAndInvisibilty()
        }
        rootView.tv_itm_select.text = count.toString() + " item selected"
    }

    override fun onResume() {

        /*if (isImage!!) {
            if(rvImagesAdapterD!!.itemCount==0){
                no_media!!.setText( "No Image Found")
            }
        } else {
            if (rvVideosAdapterD!!.itemCount == 0) {
                no_media!!.setText("No Video Found")
            }
        }*/
        super.onResume()
    }

    override fun onPause() {

        clearMultipleSlection()
        heightAndInvisibilty()
        super.onPause()
    }

    private fun clearMultipleSlection() {
        if (isImage!!) {
            val selectedImg = rvImagesAdapterD?.GetSelectedItems()
            if(selectedImg!!.size>0){
                CommonUtils.rvImagesAdapterD!!.ClearSelections()
            }

        } else {
            val selectedImg = rvVideosAdapterD?.GetSelectedItems()
            if(selectedImg!!.size>0) {
                CommonUtils.rvVideosAdapterD!!.ClearSelections()
            }
        }
    }

}