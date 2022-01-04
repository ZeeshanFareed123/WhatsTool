package com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.models

import java.io.File
import java.util.*

/**
 * Created by sonu on 24/07/17.
 */
data class SectionModel(
    val sectionLabel: String,
    val itemArrayList: ArrayList<File>
)