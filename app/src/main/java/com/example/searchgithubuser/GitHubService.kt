package com.example.searchgithubuser

data class GitHubUser (val login:String, val avatar_url: String)
data class GitHubUserInfo (val userName: String)

interface GitHubService {
    suspend fun getUsers (keyword: String) : Result<List<GitHubUser>>
    suspend fun getUserInfo (userName: String) : Result<GitHubUserInfo>
}

