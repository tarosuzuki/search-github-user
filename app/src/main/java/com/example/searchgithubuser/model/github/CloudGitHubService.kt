package com.example.searchgithubuser.model.github

import android.util.Log
import com.example.searchgithubuser.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.IllegalStateException
import okhttp3.OkHttpClient

class CloudGitHubService : GitHubService {
    private var gitHubApi: GitHubApi

    init {
        val gitHubApiUserName = BuildConfig.GITHUB_API_USERNAME
        val gitHubApiAccessToken = BuildConfig.GITHUB_API_ACCESS_TOKEN
        val retrofit = if (gitHubApiUserName.isNotEmpty() && gitHubApiAccessToken.isNotEmpty()) {
            val client = OkHttpClient.Builder()
                .addInterceptor(BasicAuthInterceptor(gitHubApiUserName, gitHubApiAccessToken))
                .build()
            Retrofit.Builder().apply {
                baseUrl(gitHubEndpoint)
                addConverterFactory(GsonConverterFactory.create())
                client(client)
            }.build()
        } else {
            Retrofit.Builder().apply {
                baseUrl(gitHubEndpoint)
                addConverterFactory(GsonConverterFactory.create())
            }.build()
        }
        gitHubApi = retrofit.create(GitHubApi::class.java)
    }

    override suspend fun getUsers(keyword: String): Result<List<GitHubUser>> {
        return try {
            val response = gitHubApi.getUsers(keyword)
            Log.i(TAG, "getUsers response : ${response.userList}")
            Result.success(response.userList)
        } catch (e: Exception) {
            Log.e(TAG, "getUsers error : $e")
            Result.failure(IllegalStateException("error - $e"))
        }
    }

    override suspend fun getUserInfo(userName: String): Result<GitHubUserInfo> {
        return try {
            val response = gitHubApi.getUserInfo(userName)
            Log.i(TAG, "getUser response : $response")
            Result.success(response)
        } catch (e: Exception) {
            Log.e(TAG, "getUser error : $e")
            Result.failure(IllegalStateException("error - $e"))
        }
    }

    override suspend fun getRepositoryInfo (userName: String) : Result<List<GitHubRepositoryInfo>> {
        return try {
            val response = gitHubApi.getRepositoryInfo(userName)
            val notForkedRepositories = response.filter { !it.fork }
            Log.i(TAG, "getRepositoryInfo response : $notForkedRepositories")
            Result.success(notForkedRepositories)
        } catch (e: Exception) {
            Log.e(TAG, "getRepositoryInfo error : $e")
            Result.failure(IllegalStateException("error - $e"))
        }
    }

    companion object {
        private const val TAG = "CloudGitHubService"
    }
}