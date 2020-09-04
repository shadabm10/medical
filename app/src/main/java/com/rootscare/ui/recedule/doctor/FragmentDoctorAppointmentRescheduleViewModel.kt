package com.rootscare.ui.recedule.doctor

import android.util.Log
import com.google.gson.Gson
import com.rootscare.data.model.api.request.doctorrequest.doctorprivatesotrequest.DoctorPrivateSlotRequest
import com.rootscare.data.model.api.request.reschedule.DoctorAppointmentRescheduleRequest
import com.rootscare.data.model.api.response.reschedule.doctorreschedule.DoctorRescheduleResponse
import com.rootscare.ui.base.BaseViewModel
import com.rootscare.ui.reviewandrating.FragmentReviewAndRatingNavigator

class FragmentDoctorAppointmentRescheduleViewModel  : BaseViewModel<FragmentDoctorAppointmentRescheduleNavigator>() {

    fun apidoctorprivateslot(doctorPrivateSlotRequest: DoctorPrivateSlotRequest) {
//        val body = RequestBody.create(MediaType.parse("application/json"), "")
        val disposable = apiServiceWithGsonFactory.apidoctorprivateslot(doctorPrivateSlotRequest)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.successDoctorPrivateSlotResponse(response)
                    /* Saving access token after singup or login */
                    if (response.result!= null){
                    }

                } else {
                    Log.d("check_response", ": null response")
                }
            }, { throwable ->
                run {
                    navigator.errorGetPatientFamilyListResponse(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }

    fun apirescheduleappointment(doctorAppointmentRescheduleRequest: DoctorAppointmentRescheduleRequest) {
//        val body = RequestBody.create(MediaType.parse("application/json"), "")
        val disposable = apiServiceWithGsonFactory.apirescheduleappointment(doctorAppointmentRescheduleRequest)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.successDoctorRescheduleResponse(response)
                    /* Saving access token after singup or login */
                    if (response.result!= null){
                    }

                } else {
                    Log.d("check_response", ": null response")
                }
            }, { throwable ->
                run {
                    navigator.errorGetPatientFamilyListResponse(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }
}