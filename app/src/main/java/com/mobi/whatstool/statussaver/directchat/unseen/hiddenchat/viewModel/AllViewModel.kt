package com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.viewModel

import android.os.Environment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.db.DataRepository
import com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.db.Student
import com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.utils.FileModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File


open class AllViewModel(private var dataRepo: DataRepository) : ViewModel() {
    val liveImage = MutableLiveData<ArrayList<FileModel>>()
    val liveVideo = MutableLiveData<ArrayList<FileModel>>()
    val downloadedImage = MutableLiveData<ArrayList<FileModel>>()
    val downloadedVideo = MutableLiveData<ArrayList<FileModel>>()
    val delete = MutableLiveData<ArrayList<File>>()

    val lastMsgOfAllUsers: LiveData<List<Student>?> = dataRepo.getLastMsgAllUsers()

    fun allMsgsOfOneUsers(): LiveData<List<Student>?> {
        return dataRepo.getAllMsgsOfOneUsers()
    }

    fun insertStudent(student: Student) {
        GlobalScope.launch(Dispatchers.IO) {
            dataRepo.insertData(student)
        }
    }
    fun _deleteRow(oneUserName: String) {
        GlobalScope.launch(Dispatchers.IO) {
            dataRepo.deleteRow(oneUserName)
        }
    }
    fun updateCounterToZero(oneUserName: String) {
        GlobalScope.launch(Dispatchers.IO) {
            dataRepo.updateData(oneUserName)
        }
    }

    fun getLiveImages(WHATSAPP_STATUSES_LOCATION: String){
        GlobalScope.launch(Dispatchers.Main){
            liveImage.value =dataRepo.getImgList(File(Environment.getExternalStorageDirectory().toString() + WHATSAPP_STATUSES_LOCATION))
        }
    }
    fun getLiveVideos(WHATSAPP_STATUSES_LOCATION: String){
        GlobalScope.launch(Dispatchers.Main){
            liveVideo.value =dataRepo.getVidList(File(Environment.getExternalStorageDirectory().toString() + WHATSAPP_STATUSES_LOCATION))
        }
    }
    fun getDownlodedImages(WHATSAPP_STATUSES_LOCATION: String){
        GlobalScope.launch(Dispatchers.Main){
            downloadedImage.value =dataRepo.getImgList(File(Environment.getExternalStorageDirectory().toString() + WHATSAPP_STATUSES_LOCATION))
        }
    }
    fun getDownlodedVideos(WHATSAPP_STATUSES_LOCATION: String){
        GlobalScope.launch(Dispatchers.Main){
            downloadedVideo.value =dataRepo.getVidList(File(Environment.getExternalStorageDirectory().toString() + WHATSAPP_STATUSES_LOCATION))
        }
    }
    fun getAllDeleted(WHATSAPP_STATUSES_LOCATION: String){
        GlobalScope.launch(Dispatchers.Main){
            delete.value =dataRepo.getdeletedList(File(Environment.getExternalStorageDirectory().toString() + WHATSAPP_STATUSES_LOCATION))
        }
    }

}