package com.interfaces

import com.rootscare.data.model.api.response.appointmenthistoryresponse.DoctorAppointmentItem

interface OnUpcommingAppointmentBtnClickListner {
    fun onCancelBtnClick(id: String)
    fun onRescheduleBtnClick(modelDoctorAppointmentItem: DoctorAppointmentItem,clickposation:String)
}