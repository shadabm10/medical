package com.rootscare.data.datasource.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rootscare.data.model.db.UserDataModel


@Dao
interface UserDao {

    @Delete
    fun delete(user: UserDataModel)

    @Query("SELECT * FROM users WHERE name LIKE :name LIMIT 1")
    fun findByName(name: String): UserDataModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: UserDataModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(users: List<UserDataModel>)

    @Query("SELECT * FROM users")
    fun loadAll(): List<UserDataModel>

    @Query("SELECT * FROM users WHERE id IN (:userIds)")
    fun loadAllByIds(userIds: List<Int>): List<UserDataModel>
}