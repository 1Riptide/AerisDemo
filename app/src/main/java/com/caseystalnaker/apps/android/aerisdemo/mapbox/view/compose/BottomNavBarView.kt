package com.caseystalnaker.apps.android.aerisdemo.mapbox.view.compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.caseystalnaker.apps.android.aerisdemo.common.BottomNavigationItem
import com.caseystalnaker.apps.android.aerisdemo.common.Screen
import com.caseystalnaker.apps.android.aerisdemo.mapbox.view.compose.alerts.AlertsScreen
import com.caseystalnaker.apps.android.aerisdemo.mapbox.view.compose.radar.RadarScreen
import com.caseystalnaker.apps.android.aerisdemo.mapbox.view.compose.storm.StormScreen
import com.caseystalnaker.apps.android.aerisdemo.utils.Places
import com.mapbox.maps.CameraState
import com.mapbox.maps.EdgeInsets
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState

/**
 * This started from an Android studio template for a bottom menu activity with
 * 3 options for changing maps and settings. Taking this as a starting point I
 * converted into a composable with the same functionality for use as a base container.
 *
 * Doing so allows us to remove layout xml files for this feature and spares us from
 * NavGraph implementations.
 *
 * One thing worth noting - this approach is more of a stress-test, in theat each
 * [Composable] view that is loaded has its own instace of a [ComposeMapboxMap].
 * Not ideal but a good learning experience. In production code this should be a single
 * view that is updated based on user-interaction.
 */
@OptIn(ExperimentalMaterial3Api::class, MapboxExperimental::class)
@Composable
fun BottomNavigationBarView() {
    val navController = rememberNavController()
    val routes = mutableListOf<String>()
    // Add a state variable for the selected index
    var selectedIndex by remember { mutableIntStateOf(0) }

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(title = {
            Text(text = "Aeris Mapbox Demo")
        })
    }, bottomBar = {
        NavigationBar {
            BottomNavigationItem().bottomNavigationItems().forEachIndexed { index, navigationItem ->
                routes.add(navigationItem.route)
                NavigationBarItem(selected = index == selectedIndex, label = {
                    Text(navigationItem.label)
                }, icon = {
                    Icon(
                        navigationItem.icon, contentDescription = navigationItem.label
                    )
                }, onClick = {
                    selectedIndex = index
                    navController.navigate(navigationItem.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                })
            }
        }
    }) { paddingValues ->
        NavHost(
            navController, startDestination = Screen.Radar.route, modifier = Modifier.padding(paddingValues = paddingValues)
        ) {
            composable(Screen.Radar.route) {
                RadarScreen(buildViewportState(Screen.Radar.route))
            }
            composable(Screen.Storm.route) {
                StormScreen(buildViewportState(Screen.Storm.route))
            }
            composable(Screen.Alerts.route) {
                AlertsScreen(buildViewportState(Screen.Alerts.route))
            }
        }
    }
}

/**
 * This method creates a custom [MapViewportState] for use with [ComposeMapboxMap]
 * by taking a route[String] and creating enhanced [CameraState]s for each route.
 *
 * @param route [String] value of [com.caseystalnaker.apps.android.aerisdemo.common.RouteEnum]
 * @return [MapViewportState]
 */
@OptIn(MapboxExperimental::class)
private fun buildViewportState(route: String?): MapViewportState {
    return when (route) {
        Screen.Radar.route -> MapViewportState(
            CameraState(
                Places.KANSAS_CITY, EdgeInsets(0.0, 0.0, 0.0, 0.0), 10.0, 0.0, 20.0
            )
        )

        Screen.Alerts.route -> MapViewportState(
            CameraState(
                Places.HELSINKI, EdgeInsets(0.0, 0.0, 0.0, 0.0), 40.0, 0.0, 50.0
            )
        )

        else -> MapViewportState(
            CameraState(
                Places.HUNTINGTON_BEACH, EdgeInsets(0.0, 0.0, 0.0, 0.0), 25.0, 50.0, 30.0
            )
        )
    }
}

