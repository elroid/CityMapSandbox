package com.elroid.dentmap.data

import android.location.Location
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
) {
    private val loc: Location by lazy {
        val location = Location("generated")
        location.latitude = latitude
        location.longitude = longitude
        location
    }

    private fun getDistanceMeters(coord: Coordinate) = loc.distanceTo(coord.loc)
    fun getDistanceKilometers(loc: Coordinate) = (getDistanceMeters(loc) / 1000)
}

data class CountryWithHome(
    val country: Country,
    val homeCountry: Country? = null
)