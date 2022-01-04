package com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.adapter

import android.content.Context
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.*


class Pager(private val myContext: Context, fm: FragmentManager, internal var totalTabs: Int) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){

    @NonNull
    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {
                return ChatAllUsers()
            }
            1 -> {
                return Deleted()
            }
            2 -> {
                return LiveStatus()
            }
            3 -> {
                return Downloaded()
            }
            4 -> {
                return DirectMsg()
            }
            else -> throw IllegalStateException("position $position is invalid for this viewpager")
        }
    }

    override fun getCount(): Int {
        return totalTabs
    }
}