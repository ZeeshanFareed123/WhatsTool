package com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.utils

import java.io.File
import java.io.Serializable


data class FileModel(var name: File? = null, var type: Int? = null):Serializable{
    companion object {
        val  Video : Int = 1
        val  Images : Int = 2
    }
}
