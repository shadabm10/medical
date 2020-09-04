package com.rootscare.ui.nurses.appointmentreschedule

import com.rootscare.data.model.api.response.nurses.nursehourlyslot.GetNurseHourlySlotResponse
import com.rootscare.data.model.api.response.nurses.nurseviewtiming.NueseViewTimingsResponse
import com.rootscare.data.model.api.response.reschedule.doctorreschedule.DoctorRescheduleResponse

interface FragmentNurseAppointmentRescheduleNavigator {
    fun successNueseViewTimingsResponse(nueseViewTimingsResponse: NueseViewTimingsResponse?)
    fun successGetNurseHourlySlotResponse(getNurseHourlySlotResponse: GetNurseHourlySlotResponse?)
    fun successDoctorRescheduleResponse(doctorRescheduleResponse: DoctorRescheduleResponse?)
    fun errorGetPatientFamilyListResponse(throwable: Throwable?)
}