package com.caseystalnaker.apps.android.aerisdemo.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.ui.graphics.vector.ImageVector

enum class RouteEnum(val path: String) {
    RADAR("Radar"),
    STORM("Storm"),
    ALERTS("Alerts")
}

/**
 * Sealed class that defines the [Screen] elements available in the demo app
 *
 * @param route [String]
 * @param label [String]
 * @param icon [ImageVector]
 *
 */
sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Radar : Screen(RouteEnum.RADAR.name, RouteEnum.RADAR.name, Icons.Default.PlayArrow)
    object Storm : Screen(RouteEnum.STORM.name, RouteEnum.STORM.name, Icons.Default.AccountCircle)
    object Alerts : Screen(RouteEnum.ALERTS.name, RouteEnum.ALERTS.name, Icons.Default.Info)
}
