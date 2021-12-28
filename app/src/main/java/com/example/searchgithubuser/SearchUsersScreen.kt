package com.example.searchgithubuser

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter


@Composable
fun SearchUsersScreen(searchUsersViewModel: SearchUsersViewModel = viewModel()) {
    val userList by searchUsersViewModel.userList.collectAsState()

    Column {
        inputKeywordBox(searchUsersViewModel)
        searchResultUserList(userList)
    }
}

private fun searchUsers(keyword: String, viewModel: SearchUsersViewModel) {
    viewModel.clearUserList()
    viewModel.fetchUserList(keyword)
}

@Composable
fun inputKeywordBox(viewModel: SearchUsersViewModel) {
    var text = rememberSaveable { mutableStateOf("")}
    val focusManager = LocalFocusManager.current

    Column {
        Text(
            stringResource(R.string.search_box_text),
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(450.dp)
        )
        OutlinedTextField(
            value = text.value,
            onValueChange = { text.value = it },
            label = { Text(stringResource(R.string.input_text_box_label),) },
            maxLines = 1,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    focusManager.clearFocus()
                    searchUsers(text.value, viewModel)
                }
            )
        )
    }
}

@Composable
fun userCard(userInfo: GitHubUser) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = rememberImagePainter(userInfo.avatar_url),
            contentDescription = null,
            modifier = Modifier.size(64.dp)
        )
        Column {
            Text(userInfo.login)
        }
    }
}

@Composable
fun searchResultUserList(userList: List<GitHubUser>) {
    userList.forEach {
        userCard(it)
    }
}

