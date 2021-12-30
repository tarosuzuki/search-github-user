package com.example.searchgithubuser.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.example.searchgithubuser.R
import com.example.searchgithubuser.model.github.GitHubUser

@Composable
fun SearchUsersScreen(
    viewModel: SearchUsersViewModel = hiltViewModel(),
    onClickUserList: (String) -> Unit = {}
) {

    val userList by viewModel.userList.collectAsState()
    val keywordText by viewModel.searchKeyword.collectAsState()

    Column {
        InputKeywordBox(
            keywordText = keywordText,
            onSearchKeywordValueChange = { keyword ->
                viewModel.setSearchKeyword(keyword)
            },
            onClickSearch = { viewModel.searchUsers() }
        )
        SearchResultUserList(
            userList = userList,
            onClickUserList = { userName ->
                viewModel.selectUser(userName)
                onClickUserList(userName)
            }
        )
    }
}

@Composable
fun InputKeywordBox(
    keywordText: String,
    onSearchKeywordValueChange: (String) -> Unit = {},
    onClickSearch: () -> Unit = {}
) {
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(R.string.search_box_text),
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
        )
        OutlinedTextField(
            value = keywordText,
            onValueChange = { onSearchKeywordValueChange(it) },
            label = { Text(stringResource(R.string.input_text_box_label)) },
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
fun SearchResultUserList(
    userList: List<GitHubUser>,
    onClickUserList: (String) -> Unit = {}
) {
    userList.forEach { userInfo ->
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable {
                onClickUserList(userInfo.login)
            }
        ) {
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
}
