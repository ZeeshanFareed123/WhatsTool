package com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


object Converters {

   /* @TypeConverter
        fun queryToString(query: Query?): String? = if (query != null) queryToString(query) else null

    @TypeConverter
        fun stringToQuery(data: String?): Query? = if (data != null) stringToQuery(data) else null
*/
    @TypeConverter
    fun fromString(value: String?): ArrayList<String> {
        val listType: Type =
            object : TypeToken<ArrayList<String?>?>() {}.getType()
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: ArrayList<String?>?): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}
