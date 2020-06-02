package com.rootscare.data.model.api.response.doctorallapiresponse.doctorbooking.doctorprivateslotresponse

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName
@JsonIgnoreProperties(ignoreUnknown = true)
data class ResultItem(
	@field:JsonProperty("time_to")
	@field:SerializedName("time_to")
	val timeTo: String? = null,
	@field:JsonProperty("time_from")
	@field:SerializedName("time_from")
	val timeFrom: String? = null,
	@field:JsonProperty("clinic_address")
	@field:SerializedName("clinic_address")
	val clinicAddress: String? = null,
	@field:JsonProperty("clinic_name")
	@field:SerializedName("clinic_name")
	val clinicName: String? = null,
	@field:JsonProperty("doctor_last_name")
	@field:SerializedName("doctor_last_name")
	val doctorLastName: String? = null,
	@field:JsonProperty("clinic_id")
	@field:SerializedName("clinic_id")
	val clinicId: String? = null,
	@field:JsonProperty("day")
	@field:SerializedName("day")
	val day: String? = null,
	@field:JsonProperty("doctor_first_name")
	@field:SerializedName("doctor_first_name")
	val doctorFirstName: String? = null
)