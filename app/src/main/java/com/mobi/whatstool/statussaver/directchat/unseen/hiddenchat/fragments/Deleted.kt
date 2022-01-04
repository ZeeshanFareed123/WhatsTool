package com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.adapter.SectionRecyclerViewAdapter
import com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.models.SectionModel
import com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.viewModel.AllViewModel
import org.koin.android.ext.android.inject
import saveit.whatsappstatussaver.whatsappsaver.Utils.CommonUtils
import java.io.File
import java.util.*
import kotlin.Comparator


class Deleted : Fragment() {
    var Deladapter: SectionRecyclerViewAdapter? = null

    private var recyclerView: RecyclerView? = null
    private val viewModel: AllViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.tab2_delete, container, false)

        recyclerView = rootView!!.findViewById(R.id.sectioned_recycler_view)

        val linearLayoutManager = LinearLayoutManager(rootView.context)
        recyclerView!!.layoutManager = linearLayoutManager

        val sectionModelArrayList =
            ArrayList<SectionModel>()

        viewModel.getAllDeleted(CommonUtils.Pos_DELETED_DIRECTORY)

        viewModel.delete.observe(this, androidx.lifecycle.Observer {
            sectionModelArrayList.clear()
            if(it.size>0){
                //Sort list of file on the bases of time
                val fileList: List<File> =
                    ArrayList()
                Collections.sort(fileList, object : Comparator<File?> {
                    override fun compare(file1: File?, file2: File?): Int {
                        val k = file1!!.lastModified() - file2!!.lastModified()
                        return if (k > 0) {
                            1
                        } else if (k == 0L) {
                            0
                        } else {
                            -1
                        }
                    }
                })
                //to get nestred recyclerView
                for (i in 1..2) {
                    val a=1
                    val itemArrayList = ArrayList<File>()
                    if (i == 1) {

                        itemArrayList.add(it.get(0))
                        //  }
                    } else {
                        it.removeAt(0)
                        for ((index, value) in it.withIndex()) {
                            itemArrayList.add(value)
                        }
                    }
                    if (i == 1) {
                        sectionModelArrayList.add(SectionModel("Recently Deleted", itemArrayList))
                    } else {
                        sectionModelArrayList.add(SectionModel("Older Deleted", itemArrayList))
                    }
                }
            }

            Deladapter = SectionRecyclerViewAdapter(this.context, sectionModelArrayList)
            recyclerView!!.adapter = Deladapter

        })



        return rootView
    }


    override fun onResume() {

        super.onResume()
    }

}// Req