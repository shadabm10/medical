package com.rootscare.ui.profile

import com.rootscare.data.model.api.response.nationalityresponse.NationalityResponse
import com.rootscare.data.model.api.response.patientprofileresponse.PatientProfileResponse

interface FragmentProfileNavigator {
    fun successPatientProfileResponse(patientProfileResponse: PatientProfileResponse?)
    fun successNationalityResponse(nationalityResponse: NationalityResponse?)
    fun errorPatientProfileResponse(throwable: Throwable?)
}