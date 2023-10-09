package com.caseystalnaker.apps.android.aerisdemo

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.casey.stalnaker.android.apps.aerisdemo.R
import com.caseystalnaker.apps.android.aerisdemo.gmaps.view.GMapsActivity
import com.caseystalnaker.apps.android.aerisdemo.gmaps.view.GoogleMapsTestActivity
import com.caseystalnaker.apps.android.aerisdemo.mapbox.view.MapboxActivity

class LandingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LandingPage { navigateTo(it) }
        }
    }

    private fun navigateTo(destination: Destination) {
        val destinationClass = when (destination) {
            Destination.Mapbox -> MapboxActivity::class.java
            Destination.AerisGoogleMaps -> GMapsActivity::class.java
            Destination.VanillaGoogleMaps -> GoogleMapsTestActivity::class.java
        }
        startActivity(Intent(this, destinationClass))
    }
}

enum class Destination {
    Mapbox, AerisGoogleMaps, VanillaGoogleMaps
}


@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    painter: Painter,
    gradientColors: List<Color>
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
        modifier = Modifier
            .background(
                Brush.verticalGradient(
                    colors = gradientColors,
                    0f,
                    600f
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(40.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.LightGray, shape = RoundedCornerShape(24.dp))
            )
            Text(text, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

@Composable
fun LandingPage(onNavigate: (Destination) -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(36.dp)
        ) {
            Button(
                onClick = {onNavigate(Destination.VanillaGoogleMaps)},
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                modifier = Modifier
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                colorResource(id = com.mapbox.maps.R.color.mapbox_location_layer_gray),
                                colorResource(id = R.color.white),
                                colorResource(id = com.mapbox.maps.R.color.mapbox_gray_dark)
                            ),
                            0f,
                            400f
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("Basic Google Map Test", fontWeight = FontWeight.Normal, color = Color.Black)
                }
            }
            CustomButton(
                text = "Aeris Google Maps Demo",
                onClick = { onNavigate(Destination.AerisGoogleMaps) },
                painter = painterResource(id = R.drawable.ic_layers),
                gradientColors = listOf(
                    colorResource(id = R.color.blue_500),
                    colorResource(id = R.color.purple_700),
                    colorResource(id = R.color.black)
                )
            )
            CustomButton(
                text = "Mapbox Demo",
                onClick = { onNavigate(Destination.Mapbox) },
                painter = painterResource(id = R.drawable.ic_layers_24dp),
                gradientColors = listOf(
                    colorResource(id = R.color.brick_300),
                    Color.Red,
                    Color.Yellow,
                    colorResource(id = R.color.canary_700)
                )
            )
        }
    }
}