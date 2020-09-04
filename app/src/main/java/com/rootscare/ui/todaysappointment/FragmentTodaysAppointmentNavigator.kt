package com.rootscare.ui.todaysappointment

import com.rootscare.data.model.api.response.appointcancelresponse.AppointmentCancelResponse
import com.rootscare.data.model.api.response.appointmenthistoryresponse.AppointmentHistoryResponse

interface FragmentTodaysAppointmentNavigator {
    fun successAppointmentHistoryResponse(appointmentHistoryResponse: AppointmentHistoryResponse?)
    fun successAppointmentCancelResponse(appointmentCancelResponse: AppointmentCancelResponse?)
    fun errorAppointmentHistoryResponse(throwable: Throwable?)
}