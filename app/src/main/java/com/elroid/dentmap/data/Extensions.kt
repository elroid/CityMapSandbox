package com.elroid.dentmap.data

import com.google.android.gms.maps.model.LatLng

fun Coordinate.toLatLng(): LatLng = LatLng(latitude, longitude)

fun Country.isEuropean(): Boolean = timezones.firstOrNull()?.startsWith("Europe") == true

fun CountryWithHome.distanceFromHomeInKm(): Int? = if (homeCountry == null) null
else country.coordinate.getDistanceKilometers(homeCountry.coordinate).toInt()
