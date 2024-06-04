package com.example.queimasegura.retrofit.model

import com.google.gson.annotations.SerializedName

data class LocationData(
    val id: Int,
    @SerializedName("location_name")
    val locationName: String,
)

data class Location(
    val status: String,
    val message: String,
    val result: List<LocationData>,
)
