package com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.db

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity(tableName = "students"
 //indices = [Index(value = ["name","msg"], unique = true)]
)

data class Student(
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "msg") var message: String,
    @ColumnInfo(name = "time") var time: String,
    @ColumnInfo(name = "count") var countMsg: String,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") var id: Long = 0

): Parcelable

