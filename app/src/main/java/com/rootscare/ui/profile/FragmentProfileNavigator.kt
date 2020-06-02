package com.rootscare.ui.profile

import com.rootscare.data.model.api.response.patientprofileresponse.PatientProfileResponse

interface FragmentProfileNavigator {
    fun successPatientProfileResponse(patientProfileResponse: PatientProfileResponse?)
    fun errorPatientProfileResponse(throwable: Throwable?)
}