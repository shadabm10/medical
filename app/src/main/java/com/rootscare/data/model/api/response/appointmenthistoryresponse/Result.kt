package com.rootscare.data.model.api.response.appointmenthistoryresponse

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName
@JsonIgnoreProperties(ignoreUnknown = true)
data class Result(
	@field:JsonProperty("caregiver_appointment")
	@field:SerializedName("caregiver_appointment")
	val caregiverAppointment: ArrayList<CaregiverAppointmentItem?>? = null,
	@field:JsonProperty("doctor_appointment")
	@field:SerializedName("doctor_appointment")
	val doctorAppointment: ArrayList<DoctorAppointmentItem?>? = null,
	@field:JsonProperty("pathology_appointment")
	@field:SerializedName("pathology_appointment")
	val pathologyAppointment: ArrayList<PathologyAppointmentItem?>? = null,
	@field:JsonProperty("nurse_appointment")
	@field:SerializedName("nurse_appointment")
	val nurseAppointment: ArrayList<NurseAppointmentItem?>? = null,
	@field:JsonProperty("physiotherapy_appointment")
	@field:SerializedName("physiotherapy_appointment")
	val physiotherapyAppointment: ArrayList<PhysiotherapyAppointmentItem?>? = null,
	@field:JsonProperty("babysitter_appointment")
	@field:SerializedName("babysitter_appointment")
	val babysitterAppointment: ArrayList<BabysitterAppointmentItem?>? = null
)