package com.example.queimasegura.retrofit.model.data

import com.google.gson.annotations.SerializedName

data class Location(
    val id: Int,
    @SerializedName("location_name")
    val locationName: String,
)
