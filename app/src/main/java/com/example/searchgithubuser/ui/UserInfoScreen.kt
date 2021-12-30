package com.example.searchgithubuser.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.example.searchgithubuser.R
import com.example.searchgithubuser.model.github.GitHubRepositoryInfo
import com.example.searchgithubuser.model.github.GitHubUserInfo

@Composable
fun UserInfoScreen(
    viewModel: SearchUsersViewModel = hiltViewModel(),
    onClickRepositoryList: (String) -> Unit = {}
) {
    val userInfo by viewModel.userInfo.collectAsState()
    val repositoryList by viewModel.repositoryList.collectAsState()

    Column {
        userInfo?.let { UserProfile(it) }
        Spacer(Modifier.height(36.dp))
        RepositoriesInfo(repositoryList, onClickRepositoryList)
    }
}

@Composable
fun UserProfile(userInfo: GitHubUserInfo) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(12.dp))
        Image(
            painter = rememberImagePainter(userInfo.avatar_url),
            contentDescription = null,
            modifier = Modifier
                .size(128.dp)
                .clip(CircleShape)
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = userInfo.login,
            style = MaterialTheme.typography.h6
        )
        if (userInfo.name != null) {
            Text(
                text = "( ${userInfo.name} )",
                style = MaterialTheme.typography.h6
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(stringResource(R.string.user_profile_followers))
            Text(userInfo.followers.toString())
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(stringResource(R.string.user_profile_following))
            Text(userInfo.following.toString())
        }
    }
}

@Composable
fun RepositoriesInfo(
    repositoryList: List<GitHubRepositoryInfo>,
    onClickRepositoryList: (String) -> Unit = {}
) {
    Row {
        Spacer(Modifier.width(12.dp))
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.user_profile_repositories_text),
                style = MaterialTheme.typography.h5
            )
            Spacer(Modifier.height(12.dp))
            LazyColumn {
                items(repositoryList) { repository ->
                    Column(
                        modifier = Modifier.clickable {
                            onClickRepositoryList(repository.html_url)
                        }
                    ) {
                        Text(repository.name, fontWeight = FontWeight.Bold)
                        if (repository.language != null) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(stringResource(R.string.user_profile_repository_language))
                                Text(repository.language, fontWeight = FontWeight.Bold)
                            }
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(stringResource(R.string.user_profile_repository_star))
                            Text(
                                repository.stargazers_count.toString(),
                                fontWeight = FontWeight.Bold
                            )
                        }
                        if (repository.description != null) {
                            Text(repository.description)
                        }
                        Spacer(Modifier.height(36.dp))
                    }
                }
            }
        }
        Spacer(Modifier.width(12.dp))
    }
}
