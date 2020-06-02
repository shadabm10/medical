package com.rootscare.data.model.api.request.doctorrequest.doctorprivatesotrequest

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName
@JsonIgnoreProperties(ignoreUnknown = true)
data class DoctorPrivateSlotRequest(
	@field:JsonProperty("date")
	@field:SerializedName("date")
	var date: String? = null,
	@field:JsonProperty("doctor_id")
	@field:SerializedName("doctor_id")
	var doctorId: String? = null
)