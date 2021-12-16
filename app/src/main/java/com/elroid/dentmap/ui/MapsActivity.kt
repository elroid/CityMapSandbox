package com.elroid.dentmap.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.elroid.dentmap.R
import com.elroid.dentmap.data.DataManager
import com.elroid.dentmap.data.toLatLng
import com.elroid.dentmap.databinding.ActivityMapsBinding
import com.elroid.dentmap.util.dpToPx
import com.github.ajalt.timberkt.d
import com.github.ajalt.timberkt.i
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private var job: CompletableJob = SupervisorJob()
    private val coroutineContext: CoroutineContext get() = Dispatchers.Main + job
    private fun withMainScope() = CoroutineScope(coroutineContext)

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

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        showCities()
    }

    private fun showCities() {
        withMainScope().launch {
            val dataManager = DataManager(applicationContext)
            val countries = dataManager.getEuropeanCountries()
            d { "Got countries:$countries" }
            val bounds = LatLngBounds.Builder()
            countries.forEachIndexed { index, country ->
                i { "country[$index]: $country" }
                val latLng = country.coordinate.toLatLng()
                val marker = MarkerOptions()
                    .position(latLng)
                    .title(country.capital)
                map.addMarker(marker)
                bounds.include(latLng)
            }
            //set map view to encompass all european cities
            val update = CameraUpdateFactory.newLatLngBounds(bounds.build(), 32.dpToPx())
            map.animateCamera(update)
        }
    }
}