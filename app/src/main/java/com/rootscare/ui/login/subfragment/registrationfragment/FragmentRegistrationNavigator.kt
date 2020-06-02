package com.rootscare.ui.login.subfragment.registrationfragment

import com.rootscare.data.model.api.response.registrationresponse.registrationcheckotpresponse.RegistrationCheckOTPResponse
import com.rootscare.data.model.api.response.registrationresponse.registrationsendotpresponse.RegistrationSendOTPResponse

interface FragmentRegistrationNavigator {
    fun successRegistrationSendOTPResponse(registrationSendOTPResponse: RegistrationSendOTPResponse?)
    fun errorRegistrationSendOTPResponse(throwable: Throwable?)
}