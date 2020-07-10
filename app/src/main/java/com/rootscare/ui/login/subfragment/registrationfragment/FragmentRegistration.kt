package com.rootscare.ui.login.subfragment.registrationfragment

import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.latikaseafood.ui.base.AppData
import com.rootscare.BR
import com.rootscare.R
import com.rootscare.data.model.api.request.registrationrequest.registrationotpcheckrequest.RegistrationCheckOTPRequest
import com.rootscare.data.model.api.request.registrationrequest.registrationotpsendrequest.RegistrationSendOTPRequest
import com.rootscare.data.model.api.response.registrationresponse.registrationcheckotpresponse.RegistrationCheckOTPResponse
import com.rootscare.data.model.api.response.registrationresponse.registrationsendotpresponse.RegistrationSendOTPResponse
import com.rootscare.databinding.FragmentRegistrationBinding
import com.rootscare.ui.base.BaseFragment
import com.rootscare.ui.login.LoginActivity
import java.util.regex.Pattern


class FragmentRegistration : BaseFragment<FragmentRegistrationBinding, FragmentRegistrationViewModel>(), FragmentRegistrationNavigator {
    private var fragmentRegistrationBinding: FragmentRegistrationBinding? = null
    private var fragmentRegistrationViewModel: FragmentRegistrationViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_registration
    override val viewModel: FragmentRegistrationViewModel
        get() {
            fragmentRegistrationViewModel = ViewModelProviders.of(this).get(FragmentRegistrationViewModel::class.java!!)
            return fragmentRegistrationViewModel as FragmentRegistrationViewModel
        }
    companion object {
        val TAG = FragmentRegistration::class.java.simpleName
        fun newInstance(): FragmentRegistration {
            val args = Bundle()
            val fragment = FragmentRegistration()
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentRegistrationViewModel!!.navigator = this
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentRegistrationBinding = viewDataBinding

        fragmentRegistrationBinding?.llMain?.setOnClickListener(View.OnClickListener {
            val inputMethodManager =
                activity!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(
                activity!!.currentFocus!!.windowToken,
                0
            )
        })
      //  (activity as LoginActivity?)!!.setCurrentItem(1, true)
       // fragmentRegistrationBinding?.llRegMainContain?.visibility=View.VISIBLE

        fragmentRegistrationBinding?.btnRootscareSendOtp?.setOnClickListener(View.OnClickListener {


            if(checkFieldValidation()) {
                if(isNetworkConnected){
                    baseActivity?.showLoading()
                    var registrationSendOTPRequest= RegistrationSendOTPRequest()
                    registrationSendOTPRequest?.firstName=fragmentRegistrationBinding?.edtRootscareRegFname?.text?.toString()?.trim()
                    registrationSendOTPRequest?.lastName=fragmentRegistrationBinding?.edtRootscareRegLname?.text?.toString()?.trim()
                    registrationSendOTPRequest?.phoneNumber=fragmentRegistrationBinding?.edtRootscareRegMobilenumber?.text?.toString()?.trim()
                    registrationSendOTPRequest?.email=fragmentRegistrationBinding?.edtRootscareRegEmail?.text?.toString()?.trim()
                    registrationSendOTPRequest?.idNumber=fragmentRegistrationBinding?.edtRootscareRegIdNumber?.text?.toString()?.trim()
                    registrationSendOTPRequest?.password=fragmentRegistrationBinding?.edtRootscareRegPassword?.text?.toString()?.trim()
                    registrationSendOTPRequest?.confirmPassword=fragmentRegistrationBinding?.edtRootscareRegConfirmPassword?.text?.toString()?.trim()


                    AppData?.registrationRequest?.firstName=fragmentRegistrationBinding?.edtRootscareRegFname?.text?.toString()?.trim()
                    AppData?.registrationRequest?.lastName=fragmentRegistrationBinding?.edtRootscareRegLname?.text?.toString()?.trim()
                    AppData?.registrationRequest?.phoneNumber=fragmentRegistrationBinding?.edtRootscareRegMobilenumber?.text?.toString()?.trim()
                    AppData?.registrationRequest?.email=fragmentRegistrationBinding?.edtRootscareRegEmail?.text?.toString()?.trim()
                    AppData?.registrationRequest?.idNumber=fragmentRegistrationBinding?.edtRootscareRegIdNumber?.text?.toString()?.trim()
                    AppData?.registrationRequest?.password=fragmentRegistrationBinding?.edtRootscareRegPassword?.text?.toString()?.trim()
                    AppData?.registrationRequest?.confirmPassword=fragmentRegistrationBinding?.edtRootscareRegConfirmPassword?.text?.toString()?.trim()

                    fragmentRegistrationViewModel?.apipatientregistrationotpsend(registrationSendOTPRequest)


                }else{
                    Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
                }
            }
        })

//        fragmentRegistrationBinding?.btnRootscarePatientregistration?.setOnClickListener(View.OnClickListener {
//
//
//          //
//        })

    }



    //Validation checking
    private fun checkFieldValidation(): Boolean {
        if (TextUtils.isEmpty(fragmentRegistrationBinding?.edtRootscareRegFname?.getText().toString().trim())) {
            Toast.makeText(activity, "Please enter your first name", Toast.LENGTH_SHORT).show()
            return false
        }
        if (TextUtils.isEmpty(fragmentRegistrationBinding?.edtRootscareRegLname?.getText().toString().trim())) {
            Toast.makeText(activity, "Please enter your last name", Toast.LENGTH_SHORT).show()
            return false
        }
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        if (!fragmentRegistrationBinding?.edtRootscareRegEmail?.getText().toString().trim().matches(emailPattern.toRegex())) {
            Toast.makeText(activity, "Invalid Email Id", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!isValidMobile(fragmentRegistrationBinding?.edtRootscareRegMobilenumber?.getText().toString().trim())) {
            Toast.makeText(activity, "Invalid mobile number", Toast.LENGTH_SHORT).show()
            return false
        }


        if (TextUtils.isEmpty(fragmentRegistrationBinding?.edtRootscareRegIdNumber?.getText().toString().trim())) {
            Toast.makeText(activity, "Please enter your ID Number", Toast.LENGTH_SHORT).show()
            return false
        }


        if (TextUtils.isEmpty(fragmentRegistrationBinding?.edtRootscareRegPassword?.getText().toString().trim())) {
            Toast.makeText(activity, "Password can not be blank", Toast.LENGTH_SHORT).show()
            return false
        }

        if (TextUtils.isEmpty(fragmentRegistrationBinding?.edtRootscareRegConfirmPassword?.getText().toString().trim())) {
            Toast.makeText(activity, "Password can not be blank", Toast.LENGTH_SHORT).show()
            return false
        }
        if(!fragmentRegistrationBinding?.edtRootscareRegConfirmPassword?.getText().toString().trim().equals(fragmentRegistrationBinding?.edtRootscareRegPassword?.getText().toString().trim())){
            Toast.makeText(activity, "Confirm password and password  does not matched", Toast.LENGTH_SHORT).show()
            return false
        }
//        if (TextUtils.isEmpty(fragmentProfileBinding?.txtAddress?.getText().toString().trim())) {
//            fragmentProfileBinding?.txtAddress?.setError(resources.getString(R.string.can_not_be_blank))
//            return false
//        }


        return true
    }

    //Mobile number validation method
    private fun isValidMobile(phone: String): Boolean {
        return !TextUtils.isEmpty(phone) && Patterns.PHONE.matcher(phone).matches()
    }

    override fun successRegistrationSendOTPResponse(registrationSendOTPResponse: RegistrationSendOTPResponse?) {
        baseActivity?.hideLoading()
        if(registrationSendOTPResponse?.code.equals("200")){
            (activity as LoginActivity?)!!.setCurrentItem(2, true)
        }else{
            Toast.makeText(activity, registrationSendOTPResponse?.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun errorRegistrationSendOTPResponse(throwable: Throwable?) {
        baseActivity?.hideLoading()
        if (throwable?.message != null) {
            Log.d(FragmentRegistration.TAG, "--ERROR-Throwable:-- ${throwable.message}")
            Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }



}