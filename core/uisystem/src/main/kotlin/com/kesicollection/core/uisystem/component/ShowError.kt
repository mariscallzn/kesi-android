package com.kesicollection.core.uisystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kesicollection.core.uisystem.R
import com.kesicollection.core.uisystem.theme.KesiTheme

@Composable
fun ShowError(
    onTryAgain: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text("📡", style = MaterialTheme.typography.headlineLarge.copy(fontSize = 120.sp))
        Spacer(Modifier.height(16.dp))
        Text(
            stringResource(R.string.core_uisystem_network_error),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(32.dp))
        Button(onTryAgain) {
            Text(stringResource(R.string.core_uisystem_try_again))
        }
    }
}

@PreviewLightDark
@Composable
private fun PreviewShowError() {
    ShowErrorExample()
}

@Composable
private fun ShowErrorExample(modifier: Modifier = Modifier) {
    KesiTheme {
        ShowError({}, modifier = modifier)
    }
}