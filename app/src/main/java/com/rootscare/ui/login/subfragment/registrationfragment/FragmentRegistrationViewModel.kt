package com.rootscare.ui.login.subfragment.registrationfragment

import android.util.Log
import com.google.gson.Gson
import com.rootscare.data.model.api.request.registrationrequest.registrationotpcheckrequest.RegistrationCheckOTPRequest
import com.rootscare.data.model.api.request.registrationrequest.registrationotpsendrequest.RegistrationSendOTPRequest
import com.rootscare.ui.base.BaseViewModel

class FragmentRegistrationViewModel : BaseViewModel<FragmentRegistrationNavigator>() {

    fun apipatientregistrationotpsend(registrationSendOTPRequest: RegistrationSendOTPRequest) {
//        val body = RequestBody.create(MediaType.parse("application/json"), "")
        val disposable = apiServiceWithGsonFactory.apipatientregistrationotpsend(registrationSendOTPRequest)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.successRegistrationSendOTPResponse(response)
                    /* Saving access token after singup or login */
                    if (response.result!= null){
                    }

                } else {
                    Log.d("check_response", ": null response")
                }
            }, { throwable ->
                run {
                    navigator.errorRegistrationSendOTPResponse(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }




}