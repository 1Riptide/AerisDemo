package com.caseystalnaker.apps.android.aerisdemo.mapbox.view.compose.alerts

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.caseystalnaker.apps.android.aerisdemo.mapbox.view.compose.ComposeMapboxMap
import com.caseystalnaker.apps.android.aerisdemo.utils.Places
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.Style
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState

@OptIn(MapboxExperimental::class)
@Composable
fun AlertsScreen(mapViewportState: MapViewportState) {
    mapViewportState.setCameraOptions {
        this.zoom(11.0)
        this.center(Places.HUNTINGTON_BEACH)
        this.pitch(45.0)
        this.bearing(0.0)
    }
    ComposeMapboxMap(
        modifier = Modifier.fillMaxSize(),
        mapViewportState = mapViewportState,
        Style.OUTDOORS,
        true
    )
}