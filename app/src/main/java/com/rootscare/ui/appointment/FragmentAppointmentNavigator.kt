package com.rootscare.ui.appointment

import com.rootscare.data.model.api.response.appointmenthistoryresponse.AppointmentHistoryResponse

interface FragmentAppointmentNavigator {
    fun successAppointmentHistoryResponse(appointmentHistoryResponse: AppointmentHistoryResponse?)
    fun errorAppointmentHistoryResponse(throwable: Throwable?)
}