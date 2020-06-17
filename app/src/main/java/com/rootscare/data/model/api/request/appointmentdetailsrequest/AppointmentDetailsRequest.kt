package com.rootscare.data.model.api.request.appointmentdetailsrequest

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName
@JsonIgnoreProperties(ignoreUnknown = true)
data class AppointmentDetailsRequest(
	@field:JsonProperty("service_type")
	@field:SerializedName("service_type")
	var serviceType: String? = null,
	@field:JsonProperty("id")
	@field:SerializedName("id")
	var id: String? = null
)