package com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface StudentDao {

    @Insert(onConflict = REPLACE)
    fun insert(student: Student)

    @Query("UPDATE students SET count= :counterPlus where id in (SELECT id from students where name = :name order by id desc LIMIT 1)")
    fun updateCountOfLastInsertRow(name: String?, counterPlus: String?)

    @Query("SELECT msg from students where name = :PreviousName order by id desc LIMIT 1")
    fun getLastMsgOfOneUser(PreviousName: String): String?

    @Query("SELECT count from students where name = :PreviousName order by id desc LIMIT 1")
    fun getPreviousCountr(PreviousName: String): String?

    @Query("SELECT * from students s WHERE id = (SELECT MAX(Id) FROM students WHERE s.name = name)")
    fun _getLastMsgOfAllUsers(): LiveData<List<Student>?>

    @Query("SELECT * from students Where name =:oneUserName")
    fun _getAllMsgsOneUsers(oneUserName: String): LiveData<List<Student>?>

    @Query("SELECT * FROM students")
    fun getAllUsers(): LiveData<List<Student>?>

    @Query("DELETE FROM students WHERE name = :name")
    fun deleteByName(name: String?)

}
