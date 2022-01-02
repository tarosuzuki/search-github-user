package com.example.searchgithubuser.model.github

/**
 * Data class of response for search-user API [GitHubService.searchUsers].
 * @property login username.
 * @property avatar_url icon url.
 */
data class GitHubUser(val login: String, val avatar_url: String)

/**
 * Data class of response for get-user API [GitHubService.getUserInfo].
 * @property avatar_url icon url.
 * @property login username.
 * @property name full name.
 * @property followers number of followers.
 * @property following number of followings.
 */
data class GitHubUserInfo(
    val avatar_url: String,
    val login: String,
    val name: String?,
    val followers: Int,
    val following: Int
)

/**
 * Data class of response for get-repository API [GitHubService.getRepositoryInfo].
 * @property name repository name.
 * @property language language.
 * @property stargazers_count number of stars.
 * @property description description,
 * @property fork whether this repository is fork repository or not.
 */
data class GitHubRepositoryInfo(
    val name: String,
    val language: String?,
    val stargazers_count: Int,
    val description: String?,
    val fork: Boolean,
    val html_url: String
)

/** Accessor of GitHub APIs */
interface GitHubService {
    /**
     * Search user with keyword.
     * @param keyword search keyword (call search API with query : "$keyword in:login").
     */
    suspend fun searchUsers(keyword: String): Result<List<GitHubUser>>

    /**
     * Get user's  information.
     * @param userName target user name
     */
    suspend fun getUserInfo(userName: String): Result<GitHubUserInfo>

    /**
     * Get user's repositories information.
     * @param userName target user name
     */
    suspend fun getRepositoryInfo(userName: String): Result<List<GitHubRepositoryInfo>>
}
