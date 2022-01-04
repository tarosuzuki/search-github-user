package com.example.searchgithubuser.model.github

import android.util.Log
import com.example.searchgithubuser.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.IllegalStateException

/** Implementation of [GitHubService]. */
class CloudGitHubService : GitHubService {
    private var gitHubApi: GitHubApi

    init {
        val gitHubApiUserName = BuildConfig.GITHUB_API_USERNAME
        val gitHubApiAccessToken = BuildConfig.GITHUB_API_ACCESS_TOKEN
        val logInterceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            logInterceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            logInterceptor.level = HttpLoggingInterceptor.Level.BASIC
        }

        val client = if (gitHubApiUserName.isNotEmpty() && gitHubApiAccessToken.isNotEmpty()) {
            OkHttpClient.Builder()
                .addInterceptor(logInterceptor)
                .addInterceptor(BasicAuthInterceptor(gitHubApiUserName, gitHubApiAccessToken))
                .build()
        } else {
            OkHttpClient.Builder()
                .addInterceptor(logInterceptor)
                .build()
        }

        val retrofit = Retrofit.Builder()
            .baseUrl(gitHubEndpoint)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        gitHubApi = retrofit.create(GitHubApi::class.java)
    }

    override suspend fun searchUsers(keyword: String): Result<List<GitHubUser>> {
        return try {
            val searchQuery = "$keyword in:login"
            val response = gitHubApi.searchUsers(searchQuery)
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

    override suspend fun getRepositoryInfo(userName: String): Result<List<GitHubRepositoryInfo>> {
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
