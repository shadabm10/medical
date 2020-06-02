package com.rootscare.data.datasource.db

import android.util.Log

import com.rootscare.data.datasource.db.AppDataBase
import com.rootscare.data.model.db.UserDataModel
import java.util.concurrent.Callable

import io.reactivex.Observable

class AppDbHelper(private val mAppDatabase: AppDataBase) {


    val allUsers: Observable<List<UserDataModel>>
        get() = Observable.fromCallable {
            Log.d("inserted_data_size", ": " + mAppDatabase.userDao().loadAll().size)
            mAppDatabase.userDao().loadAll()
        }

    fun insertUser(user: UserDataModel): Observable<Boolean> {
        return Observable.fromCallable {
            Log.d("check_inserted_data", ": " + user.name!!)
            mAppDatabase.userDao().insert(user)
            true
        }
    }
}
