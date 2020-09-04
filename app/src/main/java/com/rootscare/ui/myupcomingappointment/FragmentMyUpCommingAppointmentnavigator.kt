package com.rootscare.ui.myupcomingappointment

import com.rootscare.data.model.api.response.appointcancelresponse.AppointmentCancelResponse
import com.rootscare.data.model.api.response.appointmenthistoryresponse.AppointmentHistoryResponse

interface FragmentMyUpCommingAppointmentnavigator {
    fun successAppointmentHistoryResponse(appointmentHistoryResponse: AppointmentHistoryResponse?)
    fun successAppointmentCancelResponse(appointmentCancelResponse: AppointmentCancelResponse?)
    fun errorAppointmentHistoryResponse(throwable: Throwable?)
}