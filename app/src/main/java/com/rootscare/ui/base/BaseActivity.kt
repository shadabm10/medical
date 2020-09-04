package com.rootscare.ui.base

import android.annotation.TargetApi
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView

import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.rootscare.ui.base.BaseViewModel
import com.rootscare.ui.nointernet.NoInternetActivity
import com.rootscare.ui.nointernet.NoInternetViewModel
import com.rootscare.utils.CommonUtils
import com.rootscare.utils.NetworkUtils


abstract class BaseActivity<T : ViewDataBinding, V : BaseViewModel<*>> : AppCompatActivity(), BaseFragment.Callback {

    // TODO
    // this can probably depend on isLoading variable of BaseViewModel,
    // since its going to be common for all the activities
    private var mProgressDialog: ProgressDialog? = null
    var viewDataBinding: T? = null
        private set
    private var mViewModel: V? = null
    private var mWifiReceiver: BroadcastReceiver? = null
    private var closeOnInternetActivity: CloseOnInternetActivity? = null

    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    abstract val bindingVariable: Int

    /**
     * @return layout resource id
     */
    @get:LayoutRes
    abstract val layoutId: Int

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    abstract val viewModel: V

    val isNetworkConnected: Boolean
        get() = NetworkUtils.isNetworkConnected(applicationContext)

    override fun onFragmentAttached() {

    }

    override fun onFragmentDetached(tag: String) {

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
           // getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        performDataBinding()

        mWifiReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (isNetworkConnected) {
                    closeOnInternetActivity?.onCloseActivity()
                    reloadPageAfterConnectedToInternet()
                    Log.d("check_receiver", ": conencted")
                } else {
                    if (mViewModel is NoInternetViewModel) {

                    } else {
                        startActivity(NoInternetActivity.newIntent(context))
                    }
                    Log.d("check_receiver", ": not_conencted")
                }
            }
        }
        registerWifiReceiver()


    }

    @TargetApi(Build.VERSION_CODES.M)
    fun hasPermission(permission: String): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }
    override fun onDestroy() {
        super.onDestroy()
        unregisterWifiReceiver()

    }
    // BroadCastReceiver for network connectivity
    /* Start */
    private fun unregisterWifiReceiver() {
        unregisterReceiver(mWifiReceiver)
    }

    private fun registerWifiReceiver() {
        val filter = IntentFilter()
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(mWifiReceiver, filter)
    }
    /* End */
    /* No internet activity */
    fun setCloseOnInternetActivity(closeOnInternetActivity: CloseOnInternetActivity) {
        this.closeOnInternetActivity = closeOnInternetActivity
    }

    interface CloseOnInternetActivity {

        fun onCloseActivity()
    }
    /* End */
    // To reload page after internet connected
    protected abstract fun reloadPageAfterConnectedToInternet()



    fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun hideLoading() {
        if (mProgressDialog != null && mProgressDialog!!.isShowing) {
            mProgressDialog!!.cancel()
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    fun requestPermissionsSafely(permissions: Array<String>, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode)
        }
    }

    fun showLoading() {
        hideLoading()
        mProgressDialog = CommonUtils.showLoadingDialog(this)
    }

    private fun performDataBinding() {
        viewDataBinding = DataBindingUtil.setContentView(this, layoutId)
        this.mViewModel = if (mViewModel == null) viewModel else mViewModel
        viewDataBinding!!.setVariable(bindingVariable, mViewModel)
        viewDataBinding!!.executePendingBindings()
    }






}

