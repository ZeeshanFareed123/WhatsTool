package com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.di

import androidx.room.Room
import com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.db.DataRepository
import com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.db.StudentDatabase
import com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.viewModel.AllViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module


var appModule = module {

   single { AllViewModel(get()) }
   single { DataRepository(get()) }

   // Room Database
   single {
      Room.databaseBuilder(androidApplication(), StudentDatabase::class.java, "user-db-new").fallbackToDestructiveMigration()
         .build()
   }
   // Expose CurrencyDAO
   single { get<StudentDatabase>().studentDao() }

}
