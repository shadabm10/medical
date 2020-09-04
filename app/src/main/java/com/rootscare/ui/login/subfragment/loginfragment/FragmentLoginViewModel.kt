package com.rootscare.ui.login.subfragment.loginfragment

import android.util.Log
import com.google.gson.Gson
import com.rootscare.data.model.api.request.loginrequest.LoginRequest
import com.rootscare.ui.base.BaseViewModel

class FragmentLoginViewModel : BaseViewModel<FragmentLoginNavigator>() {
    fun apipatientlogin(loginRequest: LoginRequest) {
//        val body = RequestBody.create(MediaType.parse("application/json"), "")
        val disposable = apiServiceWithGsonFactory.apipatientlogin(loginRequest)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.successLoginResponse(response)
                    /* Saving access token after singup or login */
                    if (response.result!= null){
                        appSharedPref?.deleteUserId()
                        appSharedPref?.userId=response?.result?.userId
                        appSharedPref?.userName=response?.result?.firstName+" "+response?.result?.lastName
                        appSharedPref?.userEmail=response?.result?.email
                        appSharedPref?.userImage=response?.result?.image


                    }

                } else {
                    Log.d("check_response", ": null response")
                }
            }, { throwable ->
                run {
                    navigator.errorLoginResponse(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }
}