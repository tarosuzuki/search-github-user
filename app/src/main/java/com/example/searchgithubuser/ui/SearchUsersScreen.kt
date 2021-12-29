package com.example.searchgithubuser.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.example.searchgithubuser.R
import com.example.searchgithubuser.model.github.GitHubUser


@Composable
fun SearchUsersScreen(viewModel: SearchUsersViewModel = hiltViewModel(),
                      onSearchKeywordValueChange: (String) -> Unit = {},
                      onClickSearch: () -> Unit = {},
                      onClickUserList: (String) -> Unit = {}) {

    val userList by viewModel.userList.collectAsState()

    Column {
        InputKeywordBox(
            viewModel,
            onSearchKeywordValueChange,
            onClickSearch
        )
        SearchResultUserList(userList, viewModel, onClickUserList)
    }
}

@Composable
fun InputKeywordBox(viewModel: SearchUsersViewModel,
                    onSearchKeywordValueChange: (String) -> Unit = {},
                    onClickSearch: () -> Unit = {}) {
    val keywordText by viewModel.searchKeyword.collectAsState()
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
            value = keywordText,
            onValueChange = { onSearchKeywordValueChange(it) },
            label = { Text(stringResource(R.string.input_text_box_label),) },
            maxLines = 1,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    focusManager.clearFocus()
                    onClickSearch()
                }
            )
        )
    }
}

@Composable
fun UserCard(userInfo: GitHubUser,
             viewModel: SearchUsersViewModel,
             onClickUserList: (String) -> Unit = {}) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { onClickUserList(userInfo.login) }) {
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
fun SearchResultUserList(userList: List<GitHubUser>,
                         viewModel: SearchUsersViewModel,
                         onClickUserList: (String) -> Unit = {}) {
    userList.forEach {
        UserCard(it, viewModel, onClickUserList)
    }
}
