package com.caseystalnaker.apps.android.aerisdemo.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Data class responsible for defining custom [BottomNavigationItem] elements
 * for use in the [BottomNavigationBarView] container.
 */
data class BottomNavigationItem(
    val label: String = "", val icon: ImageVector = Icons.Default.Info, val route: String = ""
) {
    fun bottomNavigationItems(): List<BottomNavigationItem> {
        return listOf(
            BottomNavigationItem(
                label = Screen.Radar.label, icon = Icons.Default.Info, route = Screen.Radar.route
            ),
            BottomNavigationItem(
                label = Screen.Storm.label, icon = Icons.Default.Info, route = Screen.Storm.route
            ),
            BottomNavigationItem(
                label = Screen.Alerts.label, icon = Icons.Default.Info, route = Screen.Alerts.route
            ),
        )
    }
}
