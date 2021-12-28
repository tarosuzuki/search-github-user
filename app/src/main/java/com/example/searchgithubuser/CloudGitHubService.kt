package com.example.searchgithubuser

import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.IllegalStateException

class CloudGitHubService : GitHubService {
    /* ToDo : replace URL with string */
    private val retrofit = Retrofit.Builder().apply {
        baseUrl("https://api.github.com/")
        addConverterFactory(GsonConverterFactory.create())
    }.build()
    private val gitHubApi = retrofit.create(GitHubApi::class.java)

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