package com.rootscare.data.datasource.db


import androidx.room.Database
import androidx.room.RoomDatabase
import com.rootscare.data.datasource.db.dao.UserDao
import com.rootscare.data.model.db.UserDataModel


@Database(entities = [UserDataModel::class], version = 2, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {

    abstract fun userDao(): UserDao

}
