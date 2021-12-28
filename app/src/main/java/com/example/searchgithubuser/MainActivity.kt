package com.example.searchgithubuser

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.searchgithubuser.SearchGitHubUserApplication.Companion.appContext
import com.example.searchgithubuser.ui.theme.SearchGithubUsersTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SearchGitHubUserApp()
        }
    }
}

@Composable
fun SearchGitHubUserApp() {
    SearchGithubUsersTheme {
        val navController = rememberNavController()

        Scaffold(
            topBar = { Text("Search GitHub Users App") }
        ) {
            SearchGitHubUsersNavHost(navController, modifier = Modifier.padding(it))
        }
    }
}

enum class SearchGitHubUsersScreen {
    Search,
    UserInfo
}

private fun launchRepositoryPage(url: String){
    val builder = CustomTabsIntent.Builder()
    val customTabsIntent = builder.build()
    customTabsIntent.launchUrl(appContext, Uri.parse(url))
}


@Composable
fun SearchGitHubUsersNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = SearchGitHubUsersScreen.Search.name,
        modifier = modifier
    ) {
        composable(SearchGitHubUsersScreen.Search.name) {
            val viewModel: SearchUsersViewModel = hiltViewModel()
            SearchUsersScreen(
                viewModel = viewModel,
                onSearchKeywordValueChange ={ keyword ->
                    viewModel.setSearchKeyword(keyword)
                },
                onClickSearch = { viewModel.searchUsers() },
                onClickUserList = { userName ->
                    viewModel.selectUser(userName)
                    navController.navigate(SearchGitHubUsersScreen.UserInfo.name)
                }
            )
        }
        composable(SearchGitHubUsersScreen.UserInfo.name) {
            val viewModel: SearchUsersViewModel = hiltViewModel()
            UserInfoScreen(
                viewModel = viewModel,
                onClickRepositoryList = { repositoryUrl ->
                    launchRepositoryPage(repositoryUrl)
                }
            )
        }
    }
}
