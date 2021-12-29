package com.example.searchgithubuser.model.github

data class GitHubUser (val login: String, val avatar_url: String)

data class GitHubUserInfo (
    val avatar_url: String,
    val login: String,
    val name: String?,
    val followers: Int,
    val following: Int)

data class GitHubRepositoryInfo (
    val name: String,
    val language: String?,
    val stargazers_count: Int,
    val description: String?,
    val fork: Boolean,
    val html_url: String)

interface GitHubService {
    suspend fun getUsers (keyword: String) : Result<List<GitHubUser>>
    suspend fun getUserInfo (userName: String) : Result<GitHubUserInfo>
    suspend fun getRepositoryInfo (userName: String) : Result<List<GitHubRepositoryInfo>>
}

