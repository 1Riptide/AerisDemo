package com.caseystalnaker.apps.android.aerisdemo.mapbox.view.compose.storm

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.caseystalnaker.apps.android.aerisdemo.mapbox.view.compose.ComposeMapboxMap
import com.caseystalnaker.apps.android.aerisdemo.utils.Places
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.Style
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState

@OptIn(MapboxExperimental::class)
@Composable
fun StormScreen(mapViewportState: MapViewportState) {
    mapViewportState.setCameraOptions {
        this.zoom(9.0)
        this.center(Places.SAINT_PETE_FL)
        this.bearing(90.0)
    }
    ComposeMapboxMap(
        modifier = Modifier.fillMaxSize(),
        mapViewportState = mapViewportState,
        Style.SATELLITE_STREETS,
        false
    )
}