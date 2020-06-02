package com.rootscare.data.model.api.response.doctorallapiresponse.doctorbooking.bookingresponse

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName
@JsonIgnoreProperties(ignoreUnknown = true)
data class DoctorPrivateBooingResponse(
	@field:JsonProperty("result")
	@field:SerializedName("result")
	val result: Any? = null,
	@field:JsonProperty("code")
	@field:SerializedName("code")
	val code: String? = null,
	@field:JsonProperty("message")
	@field:SerializedName("message")
	val message: String? = null,
	@field:JsonProperty("status")
	@field:SerializedName("status")
	val status: Boolean? = null
)