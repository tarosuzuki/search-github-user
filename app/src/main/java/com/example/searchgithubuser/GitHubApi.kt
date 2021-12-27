package com.example.searchgithubuser

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query

data class GitHubUserList (
    @SerializedName("items")
    val userList: List<GitHubUser>
)

interface GitHubApi {
    @GET("search/users")
    suspend fun getUsers(@Query("q") q: String): GitHubUserList
}