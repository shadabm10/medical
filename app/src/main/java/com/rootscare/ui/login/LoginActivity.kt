package com.rootscare.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.latikaseafood.ui.base.AppData
import com.rootscare.BR
import com.rootscare.R
import com.rootscare.adapter.MyAdapter
import com.rootscare.data.model.api.request.registrationrequest.registrationotpsendrequest.RegistrationSendOTPRequest
import com.rootscare.databinding.ActivityLoginBinding
import com.rootscare.ui.base.BaseActivity
import com.rootscare.ui.home.subfragment.HomeFragment


class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>(), LoginNavigator {
    private var activityLoginBinding: ActivityLoginBinding? = null
    private var loginViewModel: LoginViewModel? = null
    var mAdapter: MyAdapter? = null
    private var check_for_close = false
    override val bindingVariable: Int
        get() = BR.viewModel

    override val layoutId: Int
        get() = R.layout.activity_login
    override val viewModel: LoginViewModel
        get() {
            loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java!!)
            return loginViewModel as LoginViewModel
        }
    companion object {
        val TAG = LoginActivity::class.java.simpleName

        fun newIntent(activity: Activity): Intent {
            return Intent(activity, LoginActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginViewModel!!.navigator = this
        activityLoginBinding = viewDataBinding
        AppData?.registrationRequest = RegistrationSendOTPRequest()
        mAdapter = MyAdapter(getSupportFragmentManager());
//        mPager = findViewById(R.id.viewpager);
        activityLoginBinding?.viewPager?.setAdapter(mAdapter)

    }

    fun setCurrentItem(item: Int, smoothScroll: Boolean) {
        activityLoginBinding?.viewPager?.setCurrentItem(item, smoothScroll)
    }

    override fun onBackPressed() {


        if (activityLoginBinding?.viewPager?.getCurrentItem() == 0) {
//            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()
//            Handler().postDelayed({
//                 }, 1000)

            super.onBackPressed()

        }else{
            this!!.setCurrentItem(0, true)
        }
    }

    override fun reloadPageAfterConnectedToInternet() {

    }

}
