package com.rootscare.data.model.api.response.editpatientfamilymemberresponse

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName
@JsonIgnoreProperties(ignoreUnknown = true)
data class EditFamilyMemberResponse(
	@field:JsonProperty("result")
	@field:SerializedName("result")
	val result: Result? = null,
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
@JsonIgnoreProperties(ignoreUnknown = true)
data class Result(
	@field:JsonProperty("image")
	@field:SerializedName("image")
	val image: String? = null,
	@field:JsonProperty("gender")
	@field:SerializedName("gender")
	val gender: String? = null,
	@field:JsonProperty("last_name")
	@field:SerializedName("last_name")
	val lastName: String? = null,
	@field:JsonProperty("created_by")
	@field:SerializedName("created_by")
	val createdBy: String? = null,
	@field:JsonProperty("patient_id")
	@field:SerializedName("patient_id")
	val patientId: String? = null,
	@field:JsonProperty("updated_by")
	@field:SerializedName("updated_by")
	val updatedBy: String? = null,
	@field:JsonProperty("phone_number")
	@field:SerializedName("phone_number")
	val phoneNumber: String? = null,
	@field:JsonProperty("id")
	@field:SerializedName("id")
	val id: String? = null,
	@field:JsonProperty("created_date")
	@field:SerializedName("created_date")
	val createdDate: String? = null,
	@field:JsonProperty("updated_date")
	@field:SerializedName("updated_date")
	val updatedDate: String? = null,
	@field:JsonProperty("first_name")
	@field:SerializedName("first_name")
	val firstName: String? = null,
	@field:JsonProperty("email")
	@field:SerializedName("email")
	val email: String? = null,
	@field:JsonProperty("age")
	@field:SerializedName("age")
	val age: String? = null,
	@field:JsonProperty("status")
	@field:SerializedName("status")
	val status: String? = null
)
