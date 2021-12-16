package com.elroid.dentmap.data

import androidx.annotation.Keep

@Keep
@Suppress("ArrayInDataClass") //we will only generate equals and hashcode if needed
data class Country(
    val timezones: Array<String>,
    val latlng: Array<Double>,
    val name: String,
    //todo use camelCase @field:Json(name = "country_code") val countryCode: String,
    val country_code: String,
    val capital: String,
) {
    val coordinate: Coordinate
        get() = Coordinate(latlng[0], latlng[1])
}

data class Coordinate(
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
)