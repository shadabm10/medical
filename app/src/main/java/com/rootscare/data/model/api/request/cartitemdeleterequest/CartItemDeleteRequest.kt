package com.rootscare.data.model.api.request.cartitemdeleterequest

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName
@JsonIgnoreProperties(ignoreUnknown = true)
data class CartItemDeleteRequest(
	@field:JsonProperty("id")
	@field:SerializedName("id")
	var id: String? = null
)