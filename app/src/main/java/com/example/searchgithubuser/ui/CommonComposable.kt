package com.example.searchgithubuser.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.AlertDialog
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.example.searchgithubuser.R

@Composable
fun LoadingIcon() {
    Box(
        modifier = Modifier.fillMaxSize().testTag(LoadingIconTag),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
@Composable
fun AlertModal(
    titleText: String,
    descriptionText: String,
    onClickOkButton: () -> Unit,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        title = { Text(text = titleText) },
        text = { Text(text = descriptionText) },
        confirmButton = {
            TextButton(
                onClick = { onClickOkButton() },
                modifier = Modifier.testTag(AlertModalOkButtonTag)
            ) {
                Text(stringResource(R.string.alert_modal_ok_button))
            }
        },
        dismissButton = null,
        modifier = Modifier.testTag(AlertModalTag)
    )
}

const val LoadingIconTag = "loadingIcon"
const val AlertModalTag = "alertModal"
const val AlertModalOkButtonTag = "alertModalOkButton"
