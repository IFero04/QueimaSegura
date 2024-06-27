package com.example.queimasegura.admin.fragments.home.fire.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class CreateFireDataIntent(
    val typeId: Int?,
    val reasonId: Int?,
    val selectedDate: String?,
    val selectedDateString: String?
) : Parcelable