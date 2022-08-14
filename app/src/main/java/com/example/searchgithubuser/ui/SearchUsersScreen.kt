package com.example.searchgithubuser.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
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
    val showLoadingIcon by viewModel.isLoadingSearchResult.collectAsState()
    val showErrorModal by viewModel.isVisibleErrorModal.collectAsState()
    val errorFactor by viewModel.gitHubApisErrorResponseFactor.collectAsState()
    val onDismissErrorModal = {
        viewModel.setIsVisibleErrorModal(false)
    }

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
            showLoadingIcon = showLoadingIcon,
            onClickUserList = { userName ->
                viewModel.selectUser(userName)
                onClickUserList(userName)
            }
        )
    }

    if (showErrorModal) {
        AlertModal(
            titleText = stringResource(R.string.github_api_response_error_message),
            descriptionText = errorFactor,
            onClickOkButton = { onDismissErrorModal() },
            onDismissRequest = { onDismissErrorModal() }
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
            ),
            modifier = Modifier.testTag(InputTextFiledForKeywordSearchTag)
        )
    }
}

@Composable
fun SearchResultUserList(
    userList: List<GitHubUser>,
    showLoadingIcon: Boolean,
    onClickUserList: (String) -> Unit = {}
) {
    if (showLoadingIcon) {
        LoadingIcon()
    } else {
        LazyColumn(
            Modifier
                .padding(12.dp)
                .testTag(SearchUserResultTag)
        ) {
            items(userList) { userInfo ->
                Column(
                    modifier = Modifier
                        .clickable {
                            onClickUserList(userInfo.login)
                        }
                        .testTag("$SearchUserResultTag-${userInfo.login}")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = rememberImagePainter(userInfo.avatar_url),
                            contentDescription = null,
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                        )
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(
                                text = userInfo.login,
                                style = MaterialTheme.typography.h6
                            )
                        }
                    }
                    Divider(Modifier.padding(vertical = 8.dp))
                }
            }
        }
    }
}

const val InputTextFiledForKeywordSearchTag = "inputTextFiledForKeywordSearch"
const val SearchUserResultTag = "searchUserResult"
