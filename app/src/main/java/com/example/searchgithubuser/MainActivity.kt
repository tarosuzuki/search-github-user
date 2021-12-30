package com.example.searchgithubuser

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.searchgithubuser.SearchGitHubUserApplication.Companion.appContext
import com.example.searchgithubuser.ui.SearchUsersScreen
import com.example.searchgithubuser.ui.SearchUsersViewModel
import com.example.searchgithubuser.ui.UserInfoScreen
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

private fun launchRepositoryPage(url: String){
    val builder = CustomTabsIntent.Builder()
    val customTabsIntent = builder.build()
    customTabsIntent.launchUrl(appContext, Uri.parse(url))
}


@Composable
fun SearchGitHubUsersNavHost(navController: NavHostController,
                             modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = SearchGitHubUsersScreen.Search.name,
        modifier = modifier
    ) {
        composable(SearchGitHubUsersScreen.Search.name) {
            SearchUsersScreen(
                onClickUserList = {
                    navController.navigate(SearchGitHubUsersScreen.UserInfo.name)
                }
            )
        }
        composable(SearchGitHubUsersScreen.UserInfo.name) {
            val parentEntry = remember {
                navController.getBackStackEntry(SearchGitHubUsersScreen.Search.name)
            }
            val viewModel = hiltViewModel<SearchUsersViewModel>(
                parentEntry
            )
            UserInfoScreen(
                viewModel = viewModel,
                onClickRepositoryList = { repositoryUrl ->
                    launchRepositoryPage(repositoryUrl)
                }
            )
        }
    }
}
