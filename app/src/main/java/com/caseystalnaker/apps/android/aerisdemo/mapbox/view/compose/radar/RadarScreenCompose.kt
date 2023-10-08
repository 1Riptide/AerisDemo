package com.caseystalnaker.apps.android.aerisdemo.mapbox.view.compose.radar

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.caseystalnaker.apps.android.aerisdemo.utils.Places
import com.caseystalnaker.apps.android.aerisdemo.mapbox.view.compose.ComposeMapboxMap
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.Style
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState

@OptIn(MapboxExperimental::class)
@Composable
fun RadarScreen(mapViewportState: MapViewportState) {
    mapViewportState.setCameraOptions {
        this.zoom(7.0)
        this.center(Places.KANSAS_CITY)
        this.pitch(72.0)
        this.bearing(135.0)
    }

    ComposeMapboxMap(
        modifier = Modifier.fillMaxSize(),
        mapViewportState = mapViewportState,
        Style.TRAFFIC_NIGHT,
        false
    )
}