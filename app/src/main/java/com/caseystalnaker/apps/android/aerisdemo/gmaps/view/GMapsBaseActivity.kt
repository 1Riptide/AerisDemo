package com.caseystalnaker.apps.android.aerisdemo.gmaps.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.aerisweather.aeris.maps.AerisMapView
import com.casey.stalnaker.android.apps.aerisdemo.R
import com.google.android.gms.maps.OnMapReadyCallback

abstract class GMapsBaseActivity : ComponentActivity() {

    var _aerisMapView: AerisMapView? = null

    open val initView: (callback: OnMapReadyCallback?) -> Unit = {
        /* virtual */
    }

    abstract fun loadMapsUI()
    internal fun getPermissions() {
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
            initView(this as? OnMapReadyCallback)
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
                    loadMapsUI()
                }
            }
        }
    }
}