package com.example.mylotto.util.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ContactsDao {

    @Query("select * from lotto")
    fun select():MutableList<Lotto?>

    //      충돌처리방식, 덮어쓰기
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(lotto: Lotto): Long

    @Query("UPDATE lotto SET checked =:isBoolean, price=:price, grade=:grade, content=:content WHERE number=:number and time=:time")
    fun update(isBoolean: Boolean, price: String, grade: String, content: String, number: String, time: String)

    @Query("delete from lotto where number=:number and time=:time")
    fun delete(number: String, time: String) : Int

}