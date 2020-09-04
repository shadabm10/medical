package com.rootscare.ui.nurses.appointmentreschedule

import android.util.Log
import com.google.gson.Gson
import com.rootscare.data.model.api.request.nurse.hourlyslot.NurseHourlySlotRequest
import com.rootscare.data.model.api.request.nurse.nurseslots.NurseSlotRequest
import com.rootscare.data.model.api.request.reschedule.DoctorAppointmentRescheduleRequest
import com.rootscare.ui.base.BaseViewModel

class FragmentNurseAppointmentRescheduleViewModel : BaseViewModel<FragmentNurseAppointmentRescheduleNavigator>(){
    fun taskbasedslots(nurseSlotRequestBody: NurseSlotRequest) {
//        val body = RequestBody.create(MediaType.parse("application/json"), "")
        val disposable = apiServiceWithGsonFactory.taskbasedslots(nurseSlotRequestBody)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.successNueseViewTimingsResponse(response)
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


    fun apigethourlyrates(nurseHourlySlotRequest: NurseHourlySlotRequest) {
//        val body = RequestBody.create(MediaType.parse("application/json"), "")
        val disposable = apiServiceWithGsonFactory.apigethourlyrates(nurseHourlySlotRequest)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.successGetNurseHourlySlotResponse(response)
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