package com.rootscare.ui.login.subfragment.loginfragment

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.rootscare.BR
import com.rootscare.R
import com.rootscare.data.model.api.request.loginrequest.LoginRequest
import com.rootscare.data.model.api.response.loginresponse.LoginResponse
import com.rootscare.databinding.FragmentLoginBinding
import com.rootscare.ui.base.BaseFragment
import com.rootscare.ui.home.HomeActivity
import com.rootscare.ui.login.LoginActivity
import com.rootscare.ui.login.subfragment.forgotpassword.FragmentForGotPassword
import com.rootscare.ui.login.subfragment.registrationfragment.FragmentRegistration
import java.util.regex.Pattern

class FragmentLogin : BaseFragment<FragmentLoginBinding, FragmentLoginViewModel>(), FragmentLoginNavigator {
    private var fragmentLoginBinding: FragmentLoginBinding? = null
    private var fragmentLoginViewModel: FragmentLoginViewModel? = null
    private var isLoginRemeberCheck=false
    private var loginRemember=""

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_login
    override val viewModel: FragmentLoginViewModel
        get() {
            fragmentLoginViewModel = ViewModelProviders.of(this).get(FragmentLoginViewModel::class.java!!)
            return fragmentLoginViewModel as FragmentLoginViewModel
        }

    companion object {
        val TAG = FragmentLogin::class.java.simpleName
        fun newInstance(): FragmentLogin {
            val args = Bundle()
            val fragment = FragmentLogin()
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentLoginViewModel!!.navigator = this
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentLoginBinding = viewDataBinding
        fragmentLoginViewModel?.appSharedPref?.isloginremember="false"
        fragmentLoginBinding?.checkboxLoginremember?.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                isLoginRemeberCheck=true
                loginRemember="true"
//                fragmentLoginViewModel?.appSharedPref?.isloginremember="true"
                //Do Whatever you want in isChecked
            }else{
                isLoginRemeberCheck=false
                loginRemember="false"
//                fragmentLoginViewModel?.appSharedPref?.isloginremember="false"
            }
        }




//        fragmentLoginViewModel?.appSharedPref?.isloginremember="false"
//        fragmentLoginBinding?.checkboxLoginremember?.setOnClickListener(View.OnClickListener {
//            if(isLoginRemeberCheck){
//                fragmentLoginBinding?.checkboxLoginremember?.isChecked=true
//                isLoginRemeberCheck=true
//                fragmentLoginViewModel?.appSharedPref?.isloginremember="true"
//            }else{
//                fragmentLoginBinding?.checkboxLoginremember?.isChecked=false
//                isLoginRemeberCheck=false
//                fragmentLoginViewModel?.appSharedPref?.isloginremember="false"
//            }
//        })

        fragmentLoginBinding?.btnRootscareLogin?.setOnClickListener(View.OnClickListener {
//

            val emailOrPassword=fragmentLoginBinding?.edtEmailOrPhone?.text?.toString()
            val password= fragmentLoginBinding?.edtPassword?.text?.toString()
            if(!(emailOrPassword?.isEmpty()!!)) {
//                if (emailOrPassword.contains("[a-zA-Z]") == true){
                val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
                if (emailOrPassword.matches(emailPattern.toRegex())) {

                    if(checkEmailandPasswordValidation(emailOrPassword, password!!)) {
                        val requestUserLogin = LoginRequest()
                        requestUserLogin.emailPhone = emailOrPassword
                        requestUserLogin.password = password
                        hideKeyboard()
                        if(isNetworkConnected){
                            baseActivity?.showLoading()
                            fragmentLoginViewModel!!.apipatientlogin(requestUserLogin)
                        }else{
                            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
                        }

                    }
                }

                else{
                    if(checkPhoneandPasswordValidation(emailOrPassword, password!!)){
                        val requestUserLogin = LoginRequest()
                        requestUserLogin.emailPhone = emailOrPassword
                        requestUserLogin.password = password
                        hideKeyboard()
                        if(isNetworkConnected){
                            baseActivity?.showLoading()
                            fragmentLoginViewModel!!.apipatientlogin(requestUserLogin)
                        }else{
                            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
                        }
                       // startActivity(HomeActivity.newIntent(this@LoginActivity))
                    }
                }
            }else{
                Toast.makeText(
                    activity,
                    "Email/Password can not be empty.",
                    Toast.LENGTH_LONG
                ).show()
            }

        })

        fragmentLoginBinding?.txtForgotPassword?.setOnClickListener(View.OnClickListener {
            (activity as LoginActivity?)!!.setCurrentItem(3, true)
        })
    }



    //Validation checking for email and password
    private fun checkEmailandPasswordValidation(email: String,password: String): Boolean {

        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        if (!email.matches(emailPattern.toRegex())) {
//            activityLoginBinding?.edtEmailOrPhone?.setError("Please enter valid email id")
            Toast.makeText(activity, "Please enter valid email id", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!isValidPassword(password) ) {
            Toast.makeText(activity, "Please enter Password", Toast.LENGTH_SHORT).show()
//            activityLoginBinding?.edtPassword?.setError("Please enter Password")
            return false
        }

        return true
    }



    //Validation checking for phone number and password
    private fun checkPhoneandPasswordValidation(phone: String,password: String): Boolean {


        if (!isValidMobile(phone)) {
            //  activityLoginBinding?.edtEmailOrPhone?.setError("Invalid mobile number")
            Toast.makeText(activity, "Invalid mobile number", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!isValidPassword(password) ) {
            Toast.makeText(activity, "Please enter Password", Toast.LENGTH_SHORT).show()
            //  activityLoginBinding?.edtPassword?.setError("Please enter Password")
            return false
        }

        return true
    }


    //Mobile number validation method
    private fun isValidMobile(phone: String): Boolean {
        return !TextUtils.isEmpty(phone) && Patterns.PHONE.matcher(phone).matches()
    }

    //Password validation method
    fun isValidPassword(s: String): Boolean {
        val PASSWORD_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\!\\@\\#\\$]{8,24}")

//        return !TextUtils.isEmpty(s) && PASSWORD_PATTERN.matcher(s).matches()

        return !TextUtils.isEmpty(s)
    }

    override fun successLoginResponse(loginResponse: LoginResponse?) {
        baseActivity?.hideLoading()
        if(loginResponse?.code.equals("200")){
            fragmentLoginViewModel?.appSharedPref?.isloginremember=loginRemember
            startActivity(activity?.let { HomeActivity.newIntent(it) })
            activity?.finish()
        }else{
            Toast.makeText(activity, loginResponse?.message, Toast.LENGTH_SHORT).show()
        }

    }

    override fun errorLoginResponse(throwable: Throwable?) {
        baseActivity?.hideLoading()
        if (throwable?.message != null) {
            Log.d(FragmentRegistration.TAG, "--ERROR-Throwable:-- ${throwable.message}")
            Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }
}