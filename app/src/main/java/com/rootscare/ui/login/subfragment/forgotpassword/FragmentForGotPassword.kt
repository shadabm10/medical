package com.rootscare.ui.login.subfragment.forgotpassword

import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.rootscare.BR
import com.rootscare.R
import com.rootscare.data.model.api.request.forgotpassword.forgotpasswordchangerequest.ForgotPasswordChangeRequest
import com.rootscare.data.model.api.request.forgotpassword.forgotpasswordemailrequest.ForgotPasswordSendEmailRequest
import com.rootscare.data.model.api.response.forgotpasswordresponse.forgotpasswordchangepassword.ForgotPasswordChangePasswordResponse
import com.rootscare.data.model.api.response.forgotpasswordresponse.forgotpasswordsendmailresponse.ForgotPasswordSendMailResponse
import com.rootscare.databinding.FragmentForgotPasswordBinding
import com.rootscare.ui.base.BaseFragment
import com.rootscare.ui.login.LoginActivity
import com.rootscare.ui.login.subfragment.registrationfragment.FragmentRegistration

class FragmentForGotPassword: BaseFragment<FragmentForgotPasswordBinding, FragmentForGotPasswordViewModel>(), FragmentForGotPasswordNavigator {
    private var fragmentForgotPasswordBinding: FragmentForgotPasswordBinding? = null
    private var fragmentForGotPasswordViewModel: FragmentForGotPasswordViewModel? = null
    private var registerEmailId=""
    private var sendOTP=""
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_forgot_password
    override val viewModel: FragmentForGotPasswordViewModel
        get() {
            fragmentForGotPasswordViewModel = ViewModelProviders.of(this).get(FragmentForGotPasswordViewModel::class.java!!)
            return fragmentForGotPasswordViewModel as FragmentForGotPasswordViewModel
        }

    companion object {
        val TAG = FragmentForGotPassword::class.java.simpleName
        fun newInstance(): FragmentForGotPassword {
            val args = Bundle()
            val fragment = FragmentForGotPassword()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentForGotPasswordViewModel!!.navigator = this
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentForgotPasswordBinding = viewDataBinding
        fragmentForgotPasswordBinding?.llMain?.setOnClickListener(View.OnClickListener {
            val inputMethodManager =
                activity!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(
                activity!!.currentFocus!!.windowToken,
                0
            )
        })
        fragmentForgotPasswordBinding?.llEmailContent?.visibility=View.VISIBLE

        fragmentForgotPasswordBinding?.btnForgotpasswordSendMail?.setOnClickListener(View.OnClickListener {
            val emailOrPassword=fragmentForgotPasswordBinding?.edtEmail?.text?.toString()
            if(!(emailOrPassword?.isEmpty()!!)) {
                val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
                if (emailOrPassword.matches(emailPattern.toRegex())) {

                    if(checkFieldValidation(emailOrPassword)) {
                        if(isNetworkConnected){
                            baseActivity?.showLoading()
                    var forgotPasswordSendEmailRequest= ForgotPasswordSendEmailRequest()
                            forgotPasswordSendEmailRequest?.emailId=fragmentForgotPasswordBinding?.edtEmail?.text?.toString()?.trim()
                            registerEmailId= fragmentForgotPasswordBinding?.edtEmail?.text?.toString()?.trim()!!
                    fragmentForGotPasswordViewModel?.apiforgotpasswordemail(forgotPasswordSendEmailRequest)
                }else{
                    Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
                }

                    }
                }
            }else{
                Toast.makeText(
                    activity,
                    "Email can not be empty.",
                    Toast.LENGTH_LONG
                ).show()
            }
        })

        fragmentForgotPasswordBinding?.btnForgotpasswordSubmit?.setOnClickListener(View.OnClickListener {
            if (checkFieldValidationForChangePassword()){
                if(isNetworkConnected){
                    baseActivity?.showLoading()
                    var forgotPasswordChangeRequest= ForgotPasswordChangeRequest()
                    forgotPasswordChangeRequest?.emailId=registerEmailId
                    forgotPasswordChangeRequest?.code=fragmentForgotPasswordBinding?.firstPinView?.text?.toString()
                    forgotPasswordChangeRequest?.password=fragmentForgotPasswordBinding?.edtRootscareForgotPassword?.text?.toString()
                    fragmentForGotPasswordViewModel?.apiforgotchangepassword(forgotPasswordChangeRequest)
                }else{
                    Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun successForgotPasswordSendMailResponse(forgotPasswordSendMailResponse: ForgotPasswordSendMailResponse?) {
        baseActivity?.hideLoading()
        if(forgotPasswordSendMailResponse?.code.equals("200")){
            Toast.makeText(activity, forgotPasswordSendMailResponse?.message, Toast.LENGTH_SHORT).show()
            sendOTP= forgotPasswordSendMailResponse?.result!!
            fragmentForgotPasswordBinding?.llEmailContent?.visibility=View.GONE
            fragmentForgotPasswordBinding?.llForgotOtpContain?.visibility=View.VISIBLE
        }else{
            Toast.makeText(activity, forgotPasswordSendMailResponse?.message, Toast.LENGTH_SHORT).show()
            fragmentForgotPasswordBinding?.llEmailContent?.visibility=View.VISIBLE
            fragmentForgotPasswordBinding?.llForgotOtpContain?.visibility=View.GONE
        }
    }

    override fun successForgotPasswordChangePasswordResponse(forgotPasswordChangePasswordResponse: ForgotPasswordChangePasswordResponse?) {
        baseActivity?.hideLoading()
        if (forgotPasswordChangePasswordResponse?.code.equals("200")){
            Toast.makeText(activity, forgotPasswordChangePasswordResponse?.message, Toast.LENGTH_SHORT).show()
            (activity as LoginActivity?)!!.setCurrentItem(0, true)
        }else{
            Toast.makeText(activity, forgotPasswordChangePasswordResponse?.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun errorForgotPasswordSendMailResponse(throwable: Throwable?) {
        baseActivity?.hideLoading()
        if (throwable?.message != null) {
            Log.d(FragmentRegistration.TAG, "--ERROR-Throwable:-- ${throwable.message}")
            Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkFieldValidation(email: String): Boolean {

        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        if (!email.matches(emailPattern.toRegex())) {
//            activityLoginBinding?.edtEmailOrPhone?.setError("Please enter valid email id")
            Toast.makeText(activity, "Please enter valid email id", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
    private fun checkFieldValidationForChangePassword(): Boolean {
        if (TextUtils.isEmpty(registerEmailId)) {
            Toast.makeText(activity, "Invalid Email Id", Toast.LENGTH_SHORT).show()
            return false
        }
        if (TextUtils.isEmpty(fragmentForgotPasswordBinding?.edtRootscareForgotPassword?.getText().toString().trim())) {
            Toast.makeText(activity, "Password can not be blank", Toast.LENGTH_SHORT).show()
            return false
        }
        if (TextUtils.isEmpty(fragmentForgotPasswordBinding?.edtRootscareForgotPassword?.getText().toString().trim())) {
            Toast.makeText(activity, "Password can not be blank", Toast.LENGTH_SHORT).show()
            return false
        }

        if(!fragmentForgotPasswordBinding?.firstPinView?.getText().toString().trim().equals(sendOTP)){
            Toast.makeText(activity, "Password Miss Matched", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}