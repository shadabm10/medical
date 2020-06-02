package com.rootscare.ui.login.subfragment.loginfragment

import com.rootscare.data.model.api.response.loginresponse.LoginResponse

interface FragmentLoginNavigator {
    fun successLoginResponse(loginResponse: LoginResponse?)
    fun errorLoginResponse(throwable: Throwable?)
}