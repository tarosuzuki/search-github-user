package com.example.searchgithubuser

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import androidx.browser.customtabs.CustomTabsIntent
import com.example.searchgithubuser.SearchGitHubUserApplication.Companion.appContext


private fun launchRepositoryPage(url: String){
    val builder = CustomTabsIntent.Builder()
    val customTabsIntent = builder.build()
    customTabsIntent.launchUrl(appContext, Uri.parse(url))
}

@Composable
fun UserInfoScreen(searchUsersViewModel: SearchUsersViewModel = viewModel()) {
    val userInfo by searchUsersViewModel.userInfo.collectAsState()
    val repositoryList by searchUsersViewModel.repositoryList.collectAsState()

    Column {
        Text(
            stringResource(R.string.user_profile_text),
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            textAlign = TextAlign.Center
        )
        userInfo?.let { UserProfile(it) }
        Spacer(Modifier.height(50.dp))
        Text(
            stringResource(R.string.user_profile_repositories_text),
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            textAlign = TextAlign.Center
        )
        RepositoriesInfo(repositoryList)
    }
}

@Composable
fun UserProfile(userInfo: GitHubUserInfo) {
    Column {
        Image(
            painter = rememberImagePainter(userInfo.avatar_url),
            contentDescription = null,
            modifier = Modifier.size(64.dp)
        )
        Row(verticalAlignment = Alignment.CenterVertically){
            Text(stringResource(R.string.user_profile_username))
            Text(userInfo.login, fontWeight = FontWeight.Bold)
        }
        Row(verticalAlignment = Alignment.CenterVertically){
            Text(stringResource(R.string.user_profile_fullname))
            Text(userInfo.name, fontWeight = FontWeight.Bold)
        }
        Row(verticalAlignment = Alignment.CenterVertically){
            Text(stringResource(R.string.user_profile_followers))
            Text(userInfo.followers.toString(), fontWeight = FontWeight.Bold)
        }
        Row(verticalAlignment = Alignment.CenterVertically){
            Text(stringResource(R.string.user_profile_following))
            Text(userInfo.following.toString(), fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun RepositoriesInfo(repositoryList: List<GitHubRepositoryInfo>) {
    repositoryList.forEach {
        LazyColumn {
            items(repositoryList) { repository ->
                Column(
                    modifier = Modifier.clickable {
                        Log.i("test", "@@@ click repository : ${repository.name}")
                        launchRepositoryPage(repository.html_url)
                    }
                ) {
                    Text(it.name, fontWeight = FontWeight.Bold)
                    Row(verticalAlignment = Alignment.CenterVertically){
                        Text(stringResource(R.string.user_profile_repository_language))
                        Text(it.language, fontWeight = FontWeight.Bold)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically){
                        Text(stringResource(R.string.user_profile_repository_star))
                        Text(it.stargazers_count.toString(), fontWeight = FontWeight.Bold)
                    }
                    Text(it.description)
                    Spacer(Modifier.height(30.dp))
                }

            }
        }
    }
}

