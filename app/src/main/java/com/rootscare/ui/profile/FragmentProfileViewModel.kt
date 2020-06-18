package com.rootscare.ui.profile

import android.util.Log
import com.google.gson.Gson
import com.rootscare.data.model.api.request.patientprofilerequest.PatientProfileRequest
import com.rootscare.data.model.api.request.patientprofilerequest.updateprofilelifestylerequest.ProfileLifestyleUpdateRequest
import com.rootscare.data.model.api.request.patientprofilerequest.updateprofilemedicalrequest.ProfileMedicalUpdateRequest
import com.rootscare.ui.base.BaseViewModel
import com.rootscare.ui.login.LoginNavigator
import io.reactivex.disposables.Disposable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part

class FragmentProfileViewModel : BaseViewModel<FragmentProfileNavigator>() {

    fun apipatientprofile(patientProfileRequest: PatientProfileRequest) {
//        val body = RequestBody.create(MediaType.parse("application/json"), "")
        val disposable = apiServiceWithGsonFactory.apipatientprofile(patientProfileRequest)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.successPatientProfileResponse(response)
                    /* Saving access token after singup or login */
                    if (response.result!= null){
                    }

                } else {
                    Log.d("check_response", ": null response")
                }
            }, { throwable ->
                run {
                    navigator.errorPatientProfileResponse(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }

    fun apieditpatientprofilemedical(profileMedicalUpdateRequest: ProfileMedicalUpdateRequest) {
//        val body = RequestBody.create(MediaType.parse("application/json"), "")
        val disposable = apiServiceWithGsonFactory.apieditpatientprofilemedical(profileMedicalUpdateRequest)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.successPatientProfileResponse(response)
                    /* Saving access token after singup or login */
                    if (response.result!= null){
                    }

                } else {
                    Log.d("check_response", ": null response")
                }
            }, { throwable ->
                run {
                    navigator.errorPatientProfileResponse(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }

    fun apieditpatientprofilestyle(profileLifestyleUpdateRequest: ProfileLifestyleUpdateRequest) {
//        val body = RequestBody.create(MediaType.parse("application/json"), "")
        val disposable = apiServiceWithGsonFactory.apieditpatientprofilestyle(profileLifestyleUpdateRequest)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.successPatientProfileResponse(response)
                    /* Saving access token after singup or login */
                    if (response.result!= null){
                    }

                } else {
                    Log.d("check_response", ": null response")
                }
            }, { throwable ->
                run {
                    navigator.errorPatientProfileResponse(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }


    fun apieditpatientprofilepersonal(userId: RequestBody,first_name: RequestBody,last_name: RequestBody,id_number: RequestBody,image: MultipartBody.Part? = null,age: RequestBody,address: RequestBody,gender: RequestBody
    ,nationality: RequestBody,height: RequestBody,weight: RequestBody,marital_status: RequestBody) {

//        userId: RequestBody,first_name: RequestBody,last_name: RequestBody,id_number: RequestBody,status: RequestBody,image: MultipartBody.Part? = null
//        val body = RequestBody.create(MediaType.parse("application/json"), "")
        var disposable: Disposable? = null
        if (image != null) {
            disposable = apiServiceWithGsonFactory.apieditpatientprofilepersonal(userId,first_name,last_name,id_number,image,age,address,gender,nationality,height,weight,marital_status)
                .subscribeOn(_scheduler_io)
                .observeOn(_scheduler_ui)
                .subscribe({ response ->
                    if (response != null) {
                        // Store last login time
                        Log.d("check_response", ": " + Gson().toJson(response))
                        navigator.successPatientProfileResponse(response)
                    } else {
                        Log.d("check_response", ": null response")
                    }
                }, { throwable ->
                    run {
                        navigator.errorPatientProfileResponse(throwable)
                        Log.d("check_response_error", ": " + throwable.message)
                    }
                })
        }
        compositeDisposable.add(disposable!!)
    }
}