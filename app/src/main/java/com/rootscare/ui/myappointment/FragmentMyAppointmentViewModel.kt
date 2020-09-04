package com.rootscare.ui.myappointment

import android.util.Log
import com.google.gson.Gson
import com.rootscare.data.model.api.request.appointmentrequest.AppointmentRequest
import com.rootscare.data.model.api.request.cancelappointmentrequest.CancelAppointmentRequest
import com.rootscare.ui.base.BaseViewModel

class FragmentMyAppointmentViewModel : BaseViewModel<FragmentMyAppointmentNavigator>() {

    fun apipatientallappointment(appointmentRequest: AppointmentRequest) {
//        val body = RequestBody.create(MediaType.parse("application/json"), "")
        val disposable = apiServiceWithGsonFactory.apipatientallappointment(appointmentRequest)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.successAppointmentHistoryResponse(response)
                    /* Saving access token after singup or login */
                    if (response.result!= null){
                    }

                } else {
                    Log.d("check_response", ": null response")
                }
            }, { throwable ->
                run {
                    navigator.errorAppointmentHistoryResponse(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })
        compositeDisposable.add(disposable)
    }


    fun apicancelappointment(cancelAppointmentRequest: CancelAppointmentRequest) {
//        val body = RequestBody.create(MediaType.parse("application/json"), "")
        val disposable = apiServiceWithGsonFactory.apicancelappointment(cancelAppointmentRequest)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.successAppointmentCancelResponse(response)
                    /* Saving access token after singup or login */
                    if (response.result!= null){
                    }

                } else {
                    Log.d("check_response", ": null response")
                }
            }, { throwable ->
                run {
                    navigator.errorAppointmentHistoryResponse(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }
}