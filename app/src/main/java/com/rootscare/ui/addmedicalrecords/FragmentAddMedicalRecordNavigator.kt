package com.rootscare.ui.addmedicalrecords

import com.rootscare.data.model.api.response.doctorallapiresponse.doctorbooking.getpatientfamilymemberlistresponse.GetPatientFamilyListResponse
import com.rootscare.data.model.api.response.medicalfiledeleteresponse.MedicalFileDeleteResponse

interface FragmentAddMedicalRecordNavigator {
    fun successMedicalFileDeleteResponse(medicalFileDeleteResponse: MedicalFileDeleteResponse?)
    fun errorMedicalFileDeleteResponse(throwable: Throwable?)
}