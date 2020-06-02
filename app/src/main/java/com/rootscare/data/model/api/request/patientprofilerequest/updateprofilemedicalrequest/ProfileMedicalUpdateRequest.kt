package com.rootscare.data.model.api.request.patientprofilerequest.updateprofilemedicalrequest

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName
@JsonIgnoreProperties(ignoreUnknown = true)
data class ProfileMedicalUpdateRequest(
	@field:JsonProperty("allergies")
	@field:SerializedName("allergies")
	var allergies: String? = null,
	@field:JsonProperty("current_medication")
	@field:SerializedName("current_medication")
	var currentMedication: String? = null,
	@field:JsonProperty("user_id")
	@field:SerializedName("user_id")
	var userId: String? = null,
	@field:JsonProperty("injuries")
	@field:SerializedName("injuries")
	var injuries: String? = null,
	@field:JsonProperty("surgeries")
	@field:SerializedName("surgeries")
	var surgeries: String? = null,
	@field:JsonProperty("past_medication")
	@field:SerializedName("past_medication")
	var pastMedication: String? = null,
	@field:JsonProperty("chronic_diseases")
	@field:SerializedName("chronic_diseases")
	var chronicDiseases: String? = null
)