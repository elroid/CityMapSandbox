package com.elroid.dentmap.data

import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
@Suppress("ArrayInDataClass") //we will only generate equals and hashcode if needed
data class Country(
    val timezones:Array<String>,
    val latlng:Array<Float>,
    val name:String,
    @field:Json(name = "country_code")
    val countryCode:String,
    val capital:String,
)
