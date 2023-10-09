package com.caseystalnaker.apps.android.aerisdemo.gmaps.view

import android.location.Location
import android.os.Bundle
import android.util.AttributeSet
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.aerisweather.aeris.communication.EndpointType
import com.aerisweather.aeris.location.LocationHelper
import com.aerisweather.aeris.maps.AerisMapContainerView
import com.aerisweather.aeris.maps.AerisMapOptions
import com.aerisweather.aeris.maps.interfaces.OnAerisMapLongClickListener
import com.aerisweather.aeris.maps.interfaces.OnAerisMarkerInfoWindowClickListener
import com.aerisweather.aeris.maps.markers.AerisMarker
import com.aerisweather.aeris.model.AerisPermissions
import com.aerisweather.aeris.model.AerisResponse
import com.aerisweather.aeris.response.*
import com.aerisweather.aeris.tiles.*
import com.casey.stalnaker.android.apps.aerisdemo.R
import com.caseystalnaker.apps.android.aerisdemo.gmaps.data.room.MyPlace
import com.caseystalnaker.apps.android.aerisdemo.gmaps.view.compose.ComposeSnackbar
import com.caseystalnaker.apps.android.aerisdemo.gmaps.view.map.TemperatureInfoData
import com.caseystalnaker.apps.android.aerisdemo.gmaps.view.map.TemperatureWindowAdapter
import com.caseystalnaker.apps.android.aerisdemo.gmaps.view.viewmodel.BaseWeatherEvent
import com.caseystalnaker.apps.android.aerisdemo.gmaps.view.viewmodel.MyPlaceEvent
import com.caseystalnaker.apps.android.aerisdemo.gmaps.view.viewmodel.UnitEvent
import com.caseystalnaker.apps.android.aerisdemo.gmaps.view.viewmodel.WeatherViewModel
import com.caseystalnaker.apps.android.aerisdemo.utils.FormatUtil
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
/**
 * This Implementation borrows heavily from the AerisSDK demo and is intended to serve as
 * a natural foundation for exploration of the Aeris Android SDK.
 *
 * **See Also:** [AerisSdkDemo]( https://github.com/aerisweather/AerisAndroidSDK/tree/master/Kotlin/AerisSdkDemo)
 *
 */
@Suppress("DEPRECATION")
@AndroidEntryPoint
class GMapsActivity : GMapsBaseActivity(), OnAerisMapLongClickListener, OnAerisMarkerInfoWindowClickListener, OnMapReadyCallback {

    private var marker: Marker? = null
    private var aerisAmp: AerisAmp? = null
    private var isMapReady = false
    private var isAmpReady = false
    private var mapOptions: AerisMapOptions? = null
    private var googleMap: GoogleMap? = null
    private var infoAdapter: TemperatureWindowAdapter? = null
    private var myPlace: MyPlace? = null
    private var isMetrics: Boolean? = null
    private var attributeSet: AttributeSet? = null
    private val viewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        attributeSet = this.resources.getXml(R.xml.map_view)
        getPermissions()
    }

    override fun loadMapsUI() {
        setContent {
            MaterialTheme {
                //GMapsBottomNav() // Original intentions.
                ComposeMap(savedInstanceState = null)
            }
        }
    }

    override val initView: (callback: OnMapReadyCallback?) -> Unit = {
        it?.let {
            _aerisMapView?.getMapAsync(it)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.let {
            it.unitEvent.observe(this, ::onUnitEvent)
            it.locationEvent.observe(this, ::onLocationEvent)
            it.event.observe(this, ::onBaseWeatherEvent)
            if (myPlace == null) {
                it.requestMyPlace()
            }
        }

        //we are resuming the map view, so check for updated options
        _aerisMapView?.apply {
            mapOptions?.let {
                it.getMapPreferences(context)
                map.mapType = it.mapType
                addLayer(it.aerisAMP)
                addLayer(it.pointData)
                addLayer(it.polygonData)
            }
            //tell the map to redraw itself
            onResume()
        }
    }

    @Composable
    fun ComposeMap(savedInstanceState: Bundle?) {
        AndroidView(modifier = Modifier.fillMaxSize(), factory = { context ->
            AerisMapContainerView(context, attributeSet).apply {
                _aerisMapView = aerisMapView
                _aerisMapView?.onCreate(savedInstanceState)

                aerisAmp = AerisAmp(
                    getString(R.string.aerisapi_client_id), getString(R.string.aerisapi_client_secret)
                )

                aerisAmp?.apply {
                    //get all the possible layers, then get permissions from the API and generate a list of permissible layers
                    AerisAmpGetLayersTask(
                        GetLayersTaskCallback(), this
                    ).execute().get()
                }
                onResume()
            }
        })
    }

    private fun onUnitEvent(unit: UnitEvent) {
        isMetrics = when (unit) {
            is UnitEvent.Imperial -> false
            is UnitEvent.Metrics -> true
        }
    }

    override fun onMapReady(map: GoogleMap) {
        isMapReady = true
        googleMap = map
        _aerisMapView?.init(googleMap)
        if (isAmpReady) {
            initMap()
        }
    }

    inner class GetLayersTaskCallback : AerisAmpOnGetLayersTaskCompleted {
        override fun onAerisAmpGetLayersTaskCompleted(
            permissibleLayers: ArrayList<AerisAmpLayer>, permissions: AerisPermissions
        ) {
            isAmpReady = true
            if (isMapReady) {
                initMap()
            }
        }
    }

    private fun initMap() {
        _aerisMapView?.setUseMapOptions(true)

        //create a new MapOptions obj
        mapOptions = AerisMapOptions()
        aerisAmp?.getPermissibleLayers(false)

        //set the mapOptions class's AerisAMP obj
        mapOptions?.let {

            //it.mapType = GoogleMap.MAP_TYPE_TERRAIN
            it.aerisAMP = aerisAmp
            if (!it.getMapPreferences(this)) {
                //set default layers/data
                it.setDefaultAmpLayers()
                it.pointData = AerisPointData.NONE
                it.polygonData = AerisPolygonData.NONE

                //save the map options
                it.saveMapPreferences(this)
            }
            _aerisMapView?.map?.mapType = it.mapType
        }
        //amp layer(s)
        val aerisAmp = mapOptions?.aerisAMP

        val customAmpLayer = AerisAmpLayer("pressure-msl-nam", "pressure-msl-nam", 40)
        aerisAmp?.setLayer(customAmpLayer)

        val statesAmpLayer = aerisAmp?.getLayerFromId("states")
        val stateModifier = statesAmpLayer?.getLayerModifier("States Outlines")
        stateModifier?.setModifierOption("outlines", true)
        aerisAmp?.setLayer(statesAmpLayer)

        val outlookAmpLayer = AerisAmpLayer("temperatures-outlook-6-10d-cpc","temperatures-outlook-6-10d-cpc",70)
        outlookAmpLayer.setCustomLayerLegend(R.drawable.legend_temp_outlook)
        aerisAmp?.setLayer(outlookAmpLayer)

        _aerisMapView?.addLayer(aerisAmp)

        //point data layer(s)
        _aerisMapView?.addLayer(mapOptions?.pointData)
        _aerisMapView?.addLayer(AerisPointData.TROPICAL_CYCLONES)

        if ((aerisAmp?.activeMapLayers?.size ?: 0) < 1) {
            aerisAmp?.setDefaultLayers()
        }

        //get a new marker option object
        val markerOptions = MarkerOptions()

        //get a stored location if there is one
        var myLocation: Location? = null

        myPlace?.let {
            //we found a stored location so move the map to it
            _aerisMapView?.moveToLocation(LatLng(it.latitude, it.longitude), 9f)

            //set the marker location
            markerOptions.position(LatLng(it.latitude, it.longitude))
        } ?: run {
            //we didn't find a stored location, so get the current location
            LocationHelper(this).apply {
                myLocation = currentLocation
            }

            //move the map to the location
            _aerisMapView?.moveToLocation(myLocation, 9f)

            //set the marker location
            myLocation?.apply {
                markerOptions.position(
                    LatLng(
                        latitude, longitude
                    )
                )
            }
        }

        //add the marker with specified options
        googleMap?.let {
            myLocation?.apply {
                it.addMarker(markerOptions)
            }
        }

        //do something when a user makes a long click
        _aerisMapView?.setOnAerisMapLongClickListener(this)

        // setup the custom info window adapter to use
        infoAdapter = TemperatureWindowAdapter(this)
        _aerisMapView?.addWindowInfoAdapter(infoAdapter)
    }

    private fun onLocationEvent(event: MyPlaceEvent) {
        myPlace = (event as MyPlaceEvent.Current).myPlace
        myPlace?.let {
            if (isMapReady) _aerisMapView?.moveToLocation(LatLng(it.latitude, it.longitude), 9f)
            else _aerisMapView?.getMapAsync(this)
        }
    }

    private fun onBaseWeatherEvent(event: BaseWeatherEvent) {
        when (event) {
            is BaseWeatherEvent.Map -> loadObservation(event)
            is BaseWeatherEvent.InProgress -> {
                // Do nothing
            }

            is BaseWeatherEvent.Error -> {
                setContent {
                    ComposeSnackbar(event.msg)
                }
            }
            else -> {
                //error ?
            }
        }
    }

    private fun loadObservation(event: BaseWeatherEvent.Map) {
        val obResponse = event.response
        val ob = obResponse?.observation
        val relativeTo = obResponse?.relativeTo
        marker?.remove()

        ob?.let {
            relativeTo?.let {

                val data = TemperatureInfoData(
                    ob.icon, FormatUtil.printDegree(this, isMetrics, Pair(ob.tempC, ob.tempF))
                )
                marker = infoAdapter?.addGoogleMarker(

                    _aerisMapView?.map, relativeTo.lat, relativeTo.lon, BitmapDescriptorFactory.fromResource(R.drawable.map_indicator_blank), data
                )
                marker?.showInfoWindow()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        _aerisMapView?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        _aerisMapView?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        _aerisMapView?.onLowMemory()
    }

    override fun onMapLongClick(lat: Double, longitude: Double) {
        viewModel.requestByMapLatLong(lat, longitude)
    }

    fun onResult(type: EndpointType, response: AerisResponse) {
        if (type == EndpointType.OBSERVATIONS) {
            if (response.isSuccessfulWithResponses) {
                val obResponse = ObservationResponse(response.firstResponse)
                val ob = obResponse.observation
                val relativeTo = obResponse.relativeTo
                marker?.remove()
                val data = TemperatureInfoData(ob.icon, ob.tempF.toString())
                marker = infoAdapter?.addGoogleMarker(
                    _aerisMapView?.map, relativeTo.lat, relativeTo.lon, BitmapDescriptorFactory.fromResource(R.drawable.map_indicator_blank), data
                )
                marker?.showInfoWindow()
            }
        }
    }

    override fun earthquakeWindowPressed(response: EarthquakesResponse?, marker: AerisMarker?) {
        // do something with the response data.
        Toast.makeText(this, "Earthquake pressed!", Toast.LENGTH_SHORT).show()
    }

    override fun stormReportsWindowPressed(response: StormReportsResponse?, marker: AerisMarker?) {
        // do something with the response data.
        Toast.makeText(this, "Storm Report pressed!", Toast.LENGTH_SHORT).show()
    }

    override fun stormCellsWindowPressed(response: StormCellResponse?, marker: AerisMarker?) {
        // do something with the response data.
        Toast.makeText(this, "Storm Cell pressed!", Toast.LENGTH_SHORT).show()
    }

    override fun wildfireWindowPressed(response: FiresResponse?, marker: AerisMarker?) {
        // do something with the response data.
        Toast.makeText(this, "Wildfire pressed!", Toast.LENGTH_SHORT).show()
    }

    override fun recordsWindowPressed(response: RecordsResponse?, marker: AerisMarker?) {
        // do something with the response data.
        Toast.makeText(this, "Daily Record pressed!", Toast.LENGTH_SHORT).show()
    }

}
