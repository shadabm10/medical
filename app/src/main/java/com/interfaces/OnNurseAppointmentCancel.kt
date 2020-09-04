package com.interfaces

import com.rootscare.data.model.api.response.appointmenthistoryresponse.DoctorAppointmentItem
import com.rootscare.data.model.api.response.appointmenthistoryresponse.NurseAppointmentItem

interface OnNurseAppointmentCancel {
    fun onCancelBtnClick(id: String)
    fun onRescheduleBtnClick(nurseAppointmentItem: NurseAppointmentItem,clickposation:String)
}