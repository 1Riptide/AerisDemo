package com.caseystalnaker.apps.android.aerisdemo.gmaps.view.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Snackbar
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.casey.stalnaker.android.apps.aerisdemo.R

@Composable
fun ComposeSnackbar(msg: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 100.dp, 0.dp, 0.dp)
            .height(100.dp)
    ) {
        val snackbarVisible = remember { mutableStateOf(false) }
        Snackbar(action = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("snack_error"),
                contentAlignment = Alignment.CenterEnd
            ) {
                IconButton(onClick = {
                    snackbarVisible.value = false
                }) {
                    Icon(
                        painterResource(id = R.drawable.ic_error),
                        contentDescription = stringResource(R.string.error),
                        tint = Color.White,
                    )
                }
            }
            Text(text = stringResource(R.string.request_failed, msg), color = Color.White)

        }, modifier = Modifier.padding(10.dp)) {
            Text(text = stringResource(R.string.request_failed, msg), color = Color.White)
        }
    }
}

