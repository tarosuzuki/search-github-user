package com.example.searchgithubuser.model.github

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

data class GitHubUserList(
    @SerializedName("items")
    val userList: List<GitHubUser>
)

/** Retrofit API Declaration for GitHub APIs */
interface GitHubApi {
    @GET("search/users")
    suspend fun searchUsers(@Query("q") query: String): GitHubUserList

    @GET("users/{user}")
    suspend fun getUserInfo(@Path("user") user: String): GitHubUserInfo

    @GET("users/{user}/repos")
    suspend fun getRepositoryInfo(@Path("user") user: String): List<GitHubRepositoryInfo>
}
