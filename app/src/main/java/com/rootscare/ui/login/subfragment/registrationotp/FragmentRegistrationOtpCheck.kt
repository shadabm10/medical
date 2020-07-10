package com.rootscare.ui.login.subfragment.registrationotp

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.latikaseafood.ui.base.AppData
import com.rootscare.BR
import com.rootscare.R
import com.rootscare.data.model.api.request.registrationrequest.registrationotpcheckrequest.RegistrationCheckOTPRequest
import com.rootscare.data.model.api.response.registrationresponse.registrationcheckotpresponse.RegistrationCheckOTPResponse
import com.rootscare.databinding.FragmentRegistrationOtpBinding
import com.rootscare.ui.base.BaseFragment
import com.rootscare.ui.login.LoginActivity
import com.rootscare.ui.login.subfragment.registrationfragment.FragmentRegistration

class FragmentRegistrationOtpCheck: BaseFragment<FragmentRegistrationOtpBinding, FragmentRegistrationOtpCheckViewModel>(), FragmentRegistrationOtpCheckNavigator {

    private var fragmentRegistrationOtpBinding: FragmentRegistrationOtpBinding? = null
    private var fragmentRegistrationOtpCheckViewModel: FragmentRegistrationOtpCheckViewModel? = null

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_registration_otp
    override val viewModel: FragmentRegistrationOtpCheckViewModel
        get() {
            fragmentRegistrationOtpCheckViewModel = ViewModelProviders.of(this).get(FragmentRegistrationOtpCheckViewModel::class.java!!)
            return fragmentRegistrationOtpCheckViewModel as FragmentRegistrationOtpCheckViewModel
        }

    companion object {
        val TAG = FragmentRegistrationOtpCheck::class.java.simpleName
        fun newInstance(): FragmentRegistrationOtpCheck {
            val args = Bundle()
            val fragment = FragmentRegistrationOtpCheck()
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentRegistrationOtpCheckViewModel!!.navigator = this
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentRegistrationOtpBinding = viewDataBinding

        fragmentRegistrationOtpBinding?.llMain?.setOnClickListener(View.OnClickListener {
            val inputMethodManager =
                activity!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(
                activity!!.currentFocus!!.windowToken,
                0
            )
        })

        fragmentRegistrationOtpBinding?.btnRootscarePatientregistration?.setOnClickListener(View.OnClickListener {
//            Toast.makeText(activity, fragmentRegistrationOtpBinding?.firstPinView?.text?.toString(), Toast.LENGTH_SHORT).show()

            if(!fragmentRegistrationOtpBinding?.firstPinView?.text?.toString().equals("")){
                if(isNetworkConnected){
                    baseActivity?.showLoading()

                    var registrationCheckOTPRequest= RegistrationCheckOTPRequest()

                    registrationCheckOTPRequest?.firstName= AppData?.registrationRequest?.firstName
                    registrationCheckOTPRequest?.lastName=AppData?.registrationRequest?.lastName
                    registrationCheckOTPRequest?.phoneNumber=AppData?.registrationRequest?.phoneNumber
                    registrationCheckOTPRequest?.email=AppData?.registrationRequest?.email
                    registrationCheckOTPRequest?.idNumber=AppData?.registrationRequest?.idNumber
                    registrationCheckOTPRequest?.password=AppData?.registrationRequest?.password
                    registrationCheckOTPRequest?.confirmPassword= AppData?.registrationRequest?.confirmPassword
                    registrationCheckOTPRequest?.code=fragmentRegistrationOtpBinding?.firstPinView?.text?.toString()

                    fragmentRegistrationOtpCheckViewModel?.apipatientregistrationotpcheck(registrationCheckOTPRequest)

                }else{
                    Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
                }

            }else{
                Toast.makeText(activity, "Please enter the OTP", Toast.LENGTH_SHORT).show()
            }
        })


    }

    override fun successRegistrationCheckOTPResponse(registrationCheckOTPResponse: RegistrationCheckOTPResponse?) {
        baseActivity?.hideLoading()
        if(registrationCheckOTPResponse?.code.equals("200")){
            Toast.makeText(activity, registrationCheckOTPResponse?.message, Toast.LENGTH_SHORT).show()
            (activity as LoginActivity?)!!.setCurrentItem(0, true)
        }else{
            Toast.makeText(activity, registrationCheckOTPResponse?.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun errorRegistrationCheckOTPResponse(throwable: Throwable?) {
        baseActivity?.hideLoading()
        if (throwable?.message != null) {
            Log.d(FragmentRegistration.TAG, "--ERROR-Throwable:-- ${throwable.message}")
            Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }

}