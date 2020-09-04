package com.rootscare

import android.app.Application
import androidx.room.Room
import com.rootscare.data.datasource.api.ApiClient
import com.rootscare.data.datasource.api.ApiService
import com.rootscare.data.datasource.db.AppDataBase
import com.rootscare.data.datasource.sharedpref.AppSharedPref


import com.rootscare.utils.AppConstants

class ApplicationClass: Application() {
    var appDatabase: AppDataBase? = null
        private set
    var appSharedPref: AppSharedPref? = null
        private set




    val apiServiceWithJacksonFactory: ApiService
        get() = ApiClient.retrofit(this, getString(R.string.api_base), ApiClient.CONVERTER_TYPE_JACKSON)!!.create(ApiService::class.java!!)

    val apiServiceWithGsonFactory: ApiService
        get() = ApiClient.retrofit(this, getString(R.string.api_base), ApiClient.CONVERTER_TYPE_GSON)!!.create(ApiService::class.java!!)


    override fun onCreate() {
        super.onCreate()
        instance = this
        appSharedPref = AppSharedPref(this, AppConstants.SHARED_PREF_NAME)
        appDatabase = Room.databaseBuilder(this,
            AppDataBase::class.java, AppConstants.DB_NAME).build()
       // appEnvironment = AppEnvironment.SANDBOX


    }


   // var appEnvironment : AppEnvironment?=null

    companion object {

        @get:Synchronized
        var instance: ApplicationClass? = null
            private set

        lateinit var user:String


    }


    /*fun getAppEnvironment(): AppEnvironment? {
        return appEnvironment
    }

    fun setAppEnvironment(appEnvironment: AppEnvironment) {
        this.appEnvironment = appEnvironment
    }*/
}
