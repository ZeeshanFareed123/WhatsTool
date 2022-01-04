package com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Student::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class StudentDatabase : RoomDatabase() {
    abstract fun studentDao(): StudentDao
}

val MIGRATION_1_2: Migration = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE INDEX index_d_course_id_studio ON  students(name, msg)")
        // database.execSQL("CREATE UNIQUE INDEX index_UserRepo_id ON students (name, msg)")
        //  database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_UserRepo_id ON students (name,msg)")
    }
}
