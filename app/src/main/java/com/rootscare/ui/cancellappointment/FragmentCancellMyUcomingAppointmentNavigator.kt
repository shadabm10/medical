package com.rootscare.ui.cancellappointment

import com.rootscare.data.model.api.response.appointmenthistoryresponse.AppointmentHistoryResponse

interface FragmentCancellMyUcomingAppointmentNavigator {
    fun successAppointmentHistoryResponse(appointmentHistoryResponse: AppointmentHistoryResponse?)
    fun errorAppointmentHistoryResponse(throwable: Throwable?)
}