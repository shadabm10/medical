package com.rootscare.data.model.api.request.loginrequest

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName
@JsonIgnoreProperties(ignoreUnknown = true)
data class LoginRequest(
	@field:JsonProperty("password")
	@field:SerializedName("password")
	var password: String? = null,
	@field:JsonProperty("email_phone")
	@field:SerializedName("email_phone")
	var emailPhone: String? = null
)