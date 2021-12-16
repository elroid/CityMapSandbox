package com.elroid.dentmap.ui

import android.app.Activity
import android.view.View
import androidx.core.view.isVisible
import com.elroid.dentmap.R
import com.elroid.dentmap.data.CountryWithHome
import com.elroid.dentmap.data.distanceFromHomeInKm
import com.elroid.dentmap.databinding.ViewMapDetailBinding
import com.github.ajalt.timberkt.v
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class CityDetailWindow(private val activity: Activity) : GoogleMap.InfoWindowAdapter {

    override fun getInfoContents(marker: Marker): View {
        v { "marker tag:${marker.tag}" }
        val countryWithHome = marker.tag as CountryWithHome
        val capitalName = countryWithHome.country.capital
        val distKm = countryWithHome.distanceFromHomeInKm() ?: 0

        return ViewMapDetailBinding.inflate(activity.layoutInflater).apply {
            cityName.text = capitalName
            distance.isVisible = distKm > 0
            distance.text = activity.getString(
                R.string.distance_to_home,
                countryWithHome.homeCountry?.capital,
                distKm.toString()
            )
            homeTapInfo.text = activity.getString(R.string.mark_as_home, marker.title)
            homeTapInfo.isVisible = capitalName != countryWithHome.homeCountry?.capital ?: ""
        }.root
    }

    override fun getInfoWindow(marker: Marker): View? = null
}