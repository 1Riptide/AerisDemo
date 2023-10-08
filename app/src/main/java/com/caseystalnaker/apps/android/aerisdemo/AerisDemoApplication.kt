package com.caseystalnaker.apps.android.aerisdemo

import android.app.Application
import com.aerisweather.aeris.communication.AerisEngine
import com.aerisweather.aeris.maps.AerisMapsEngine
import com.casey.stalnaker.android.apps.aerisdemo.R
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AerisDemoApplication : Application(){

    override fun onCreate() {
        super.onCreate()
        // setting up secret key and client id for oauth to aeris
        AerisEngine.initWithKeys(
            this.getString(R.string.aerisapi_client_id),
            this.getString(R.string.aerisapi_client_secret),
            this
        )

        /*
         * can override default point parameters programmatically used on the
         * map. dt:-1 -> sorts to closest time| -4hours -> 4 hours ago. Limit is
         * a required parameter.Can also be done through the xml values in the
         * aeris_default_values.xml
         */
        AerisMapsEngine.getInstance(this).defaultPointParameters
            .setLightningParameters("dt:-1", 500, null, null)
    }
}