package com.caseystalnaker.apps.android.aerisdemo.mapbox.view.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mapbox.maps.MapDebugOptions
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState
import com.mapbox.maps.plugin.animation.CameraAnimatorType.*
import com.mapbox.maps.plugin.animation.MapAnimationOptions

/**
 * Composable [MapboxMap] implementation.
 */
@OptIn(MapboxExperimental::class)
@Composable
fun ComposeMapboxMap(
    modifier: Modifier = Modifier,
    mapViewportState: MapViewportState,
    style: String,
    debug: Boolean?
) {
    MapboxMap(
        modifier,
        mapViewportState = mapViewportState,
    ) {
        // Get reference to the raw MapView using MapEffect
        MapEffect(Unit) { mapView ->
            // enable debug mode:
            debug?.let {
                mapView.getMapboxMap().setDebug(
                    listOf(
                        MapDebugOptions.TILE_BORDERS,
                        MapDebugOptions.PARSE_STATUS,
                        MapDebugOptions.TIMESTAMPS,
                        MapDebugOptions.COLLISION,
                        MapDebugOptions.STENCIL_CLIP,
                        MapDebugOptions.DEPTH_BUFFER,
                        MapDebugOptions.MODEL_BOUNDS,
                        MapDebugOptions.TERRAIN_WIREFRAME,
                    ),
                    it
                )
            }
            mapView.getMapboxMap().loadStyle(style)
        }
        MapAnimationOptions.mapAnimationOptions { duration(5000) }
    }
}