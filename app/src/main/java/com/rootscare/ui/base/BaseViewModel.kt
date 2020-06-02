package com.rootscare.ui.base

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import com.rootscare.data.datasource.api.ApiService
import com.rootscare.data.datasource.db.AppDbHelper
import com.rootscare.data.datasource.sharedpref.AppSharedPref

import com.rootscare.ApplicationClass

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.ref.WeakReference

abstract class BaseViewModel<N> : ViewModel() {

    protected val compositeDisposable: CompositeDisposable

    val isLoading = ObservableBoolean()

    private var mNavigator: WeakReference<N>? = null

    var navigator: N
        get() = mNavigator!!.get()!!
        set(navigator) {
            this.mNavigator = WeakReference(navigator)
        }
    private val productInfo = "product_info" // "product_info";

    private val firstName = "firstname" //"firstname";

    private val isOverrideResultScreen = true


    protected val apiServiceWithJacksonFactory: ApiService
        get() = ApplicationClass.instance!!.apiServiceWithJacksonFactory

    protected val apiServiceWithGsonFactory: ApiService
        get() = ApplicationClass.instance!!.apiServiceWithGsonFactory

    val appSharedPref: AppSharedPref?
        get() = ApplicationClass.instance!!.appSharedPref

    protected val appDataBase: AppDbHelper
        get() = AppDbHelper(ApplicationClass.instance!!.appDatabase!!)


    protected val _scheduler_computation: Scheduler
        get() = Schedulers.computation()

    protected val _scheduler_io: Scheduler
        get() = Schedulers.io()

    val _scheduler_ui: Scheduler
        get() = AndroidSchedulers.mainThread()

    init {
        this.compositeDisposable = CompositeDisposable()
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    protected fun setIsLoading(isLoading: Boolean) {
        this.isLoading.set(isLoading)
    }
}
