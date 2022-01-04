package com.example.searchgithubuser.ui

import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.searchgithubuser.SearchGitHubUserApplication.Companion.appContext
import com.example.searchgithubuser.ui.theme.SearchGithubUsersTheme

enum class SearchGitHubUsersScreen(val screenTitle: String) {
    Search("Search User"),
    UserInfo("User's info");

    companion object {
        fun fromRoute(route: String?): SearchGitHubUsersScreen =
            when (route?.substringBefore("/")) {
                Search.name -> Search
                UserInfo.name -> UserInfo
                null -> Search
                else -> throw IllegalArgumentException("Route $route is not recognized.")
            }
    }
}

private fun launchRepositoryPage(url: String) {
    val builder = CustomTabsIntent.Builder()
    val customTabsIntent = builder.build()
    customTabsIntent.intent.flags = FLAG_ACTIVITY_NEW_TASK
    customTabsIntent.launchUrl(appContext, Uri.parse(url))
}

@Composable
fun SearchGitHubUserApp() {
    SearchGithubUsersTheme {
        val navController = rememberNavController()
        val backstackEntry = navController.currentBackStackEntryAsState()
        val currentScreen =
            SearchGitHubUsersScreen.fromRoute(backstackEntry.value?.destination?.route)

        Scaffold(
            topBar = {
                SearchGitHubUsersTopAppBar(
                    title = currentScreen.screenTitle,
                    onBackClick = { navController.navigateUp() }
                )
            }
        ) {
            SearchGitHubUsersNavHost(navController, modifier = Modifier.padding(it))
        }
    }
}

@Composable
fun SearchGitHubUsersTopAppBar(title: String, onBackClick: () -> Unit) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            if (title == SearchGitHubUsersScreen.UserInfo.screenTitle) {
                IconButton(onClick = { onBackClick() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back Button"
                    )
                }
            }
        }
    )
}

@Composable
fun SearchGitHubUsersNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
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
