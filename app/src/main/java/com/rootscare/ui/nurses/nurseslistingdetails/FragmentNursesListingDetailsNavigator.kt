package com.rootscare.ui.nurses.nurseslistingdetails

import com.rootscare.data.model.api.response.doctorallapiresponse.doctordepartmentlistingresponse.DoctorDepartmentListingResponse
import com.rootscare.data.model.api.response.nurses.nursedetails.NurseDetailsResponse

interface FragmentNursesListingDetailsNavigator {
    fun successNurseDetailsResponse(nurseDetailsResponse: NurseDetailsResponse?)
    fun errorNurseDetailsResponse(throwable: Throwable?)
}