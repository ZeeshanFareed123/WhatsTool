package com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.db

import android.util.Log
import androidx.lifecycle.LiveData
import com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.utils.FileModel
import saveit.whatsappstatussaver.whatsappsaver.Utils.CommonUtils
import saveit.whatsappstatussaver.whatsappsaver.Utils.CommonUtils.Companion.isRead
import saveit.whatsappstatussaver.whatsappsaver.Utils.CommonUtils.Companion.oneUserName
import java.io.File
import java.util.ArrayList


class DataRepository(private val stDao: StudentDao) {

    var aa= 0

    fun getImgList(parentDir: File): ArrayList<FileModel> {
        return  CommonUtils.getListImages(parentDir)
    }
    fun getVidList(parentDir: File): ArrayList<FileModel> {
        return  CommonUtils.getListVideos(parentDir)
    }
    fun getdeletedList(parentDir: File): ArrayList<File> {
        return  CommonUtils.getAllListDeleted(parentDir)
    }
    fun getLastMsgAllUsers(): LiveData<List<Student>?> {
        return stDao._getLastMsgOfAllUsers()
    }
     fun getAllMsgsOfOneUsers(): LiveData<List<Student>?> {
         return stDao._getAllMsgsOneUsers(oneUserName)
    }

    fun updateData(oneUserName: String) {
        stDao.updateCountOfLastInsertRow(oneUserName, "0")
    }
    fun deleteRow(oneUserName: String) {
        stDao.deleteByName(oneUserName)
    }

    fun insertData(student: Student) {
        val getMsgOfLastInsertedRow = stDao.getLastMsgOfOneUser(student.name)
        val getCountOfLastInsertedRow = stDao.getPreviousCountr(student.name)
        var _getCountOfLastInsertedRow =0

        if(getCountOfLastInsertedRow!=null ){
            _getCountOfLastInsertedRow = Integer.parseInt(getCountOfLastInsertedRow.toString())
        }
        if (getMsgOfLastInsertedRow == student.message) {
            Log.i("vvvvvvvvv", "  ${student.name}  Already Exist with PreviousMsg ${student.message}")
        } else {
            stDao.insert(student)
            /*if(oneUserName!=student.name){
                isRead==false
                aa=1
            }*/
            if(isRead==false){
                _getCountOfLastInsertedRow++
                stDao.updateCountOfLastInsertRow(student.name, _getCountOfLastInsertedRow.toString())
              /*  if(aa==1){
                    isRead==true
                    aa=0
                }*/
            }

        }
    }

    private fun gettLastMsgOfOneUser(name: String): String? {
        return stDao.getLastMsgOfOneUser(name)
    }
    private fun getPreviousCountr(name: String): String? {
        return stDao.getPreviousCountr(name)
    }
}
