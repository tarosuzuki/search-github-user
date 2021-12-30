package com.example.searchgithubuser

enum class SearchGitHubUsersScreen {
    Search,
    UserInfo;

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
