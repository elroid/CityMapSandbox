package com.elroid.dentmap.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.elroid.dentmap.R
import com.elroid.dentmap.data.Country
import com.elroid.dentmap.data.CountryWithHome
import com.elroid.dentmap.data.DataManager
import com.elroid.dentmap.data.toLatLng
import com.elroid.dentmap.databinding.ActivityMapsBinding
import com.elroid.dentmap.util.dpToPx
import com.github.ajalt.timberkt.d
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private var job: CompletableJob = SupervisorJob()
    private val coroutineContext: CoroutineContext get() = Dispatchers.Main + job
    private fun withMainScope() = CoroutineScope(coroutineContext)

    private var homeCountry: Country? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        job.complete()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        map.setInfoWindowAdapter(CityDetailWindow(this))
        map.setOnInfoWindowClickListener(this)
        showCities()
    }

    private fun showCities() {
        withMainScope().launch {
            val dataManager = DataManager(applicationContext)
            val countries = dataManager.getEuropeanCountries()
            d { "Got countries:$countries" }
            val bounds = LatLngBounds.Builder()
            countries.forEachIndexed { index, country ->
                d { "country[$index]: $country" }
                val latLng = country.coordinate.toLatLng()
                val markerOptions = MarkerOptions()
                    .position(latLng)
                    .title(country.capital)

                val marker = map.addMarker(markerOptions)
                marker?.tag = CountryWithHome(country, homeCountry)
                bounds.include(latLng)
            }
            //set map view to encompass all european cities
            val update = CameraUpdateFactory.newLatLngBounds(bounds.build(), 32.dpToPx())
            map.animateCamera(update)
        }
    }

    private fun assignHomeCountry(country: Country) {
        homeCountry = country
        map.clear()
        showCities()
    }

    override fun onInfoWindowClick(marker: Marker) {
        val markerCountry = (marker.tag as CountryWithHome).country
        assignHomeCountry(markerCountry)
    }
}