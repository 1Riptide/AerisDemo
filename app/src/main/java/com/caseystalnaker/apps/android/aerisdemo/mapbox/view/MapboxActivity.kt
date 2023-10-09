package com.caseystalnaker.apps.android.aerisdemo.mapbox.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.navigation.compose.*
import com.casey.stalnaker.android.apps.aerisdemo.R
import com.caseystalnaker.apps.android.aerisdemo.mapbox.view.compose.BottomNavigationBarView
import com.mapbox.common.MapboxOptions
import com.mapbox.common.TileStore
import com.mapbox.maps.TileStoreUsageMode
import com.mapbox.maps.mapsOptions

@SuppressLint("RestrictedApi")
class MapboxActivity : ComponentActivity() {

    // Users should keep a reference to the customised tileStore instance (if there's any)
    private val tileStore by lazy { TileStore.create() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapboxOptions.accessToken = getString(R.string.mapbox_public_access_token)
        MapboxOptions.mapsOptions.tileStore = tileStore
        MapboxOptions.mapsOptions.tileStoreUsageMode = TileStoreUsageMode.READ_ONLY
        // Permissions before rendering maps.
        getPermissions()
    }

    private fun loadMapsUI() {
        setContent {
            MaterialTheme {
                BottomNavigationBarView()
            }
        }
    }

    private fun getPermissions() {
        //check for permissions
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestMultiplePermissions.launch(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
        } else {
            loadMapsUI()
        }
    }

    private val requestMultiplePermissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        permissions.forEach { actionMap ->
            when (actionMap.key) {
                Manifest.permission.ACCESS_FINE_LOCATION -> {
                    if (actionMap.value) {
                        loadMapsUI()
                    } else {
                        Toast.makeText(
                            this, R.string.permissions_verbiage, Toast.LENGTH_LONG
                        ).show()
                    }
                }

                else -> {
                    // Yes, I'm cheating here due to time constraints. Let's show something anyway.
                    loadMapsUI()
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun MapboxPreview() {
        MaterialTheme {
            BottomNavigationBarView()
        }
    }

}