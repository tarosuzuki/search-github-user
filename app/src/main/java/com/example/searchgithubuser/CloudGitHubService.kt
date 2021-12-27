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
        try {
            val response = gitHubApi.getUsers(keyword)
            Log.i(TAG, "getUsers response : ${response.userList}")
            return Result.success(response.userList)
        } catch (e: Exception) {
            Log.e(TAG, "getUsers error : $e")
            return Result.failure(IllegalStateException("error - $e"))
        }
    }

    override suspend fun getUserInfo(userName: String): Result<GitHubUserInfo> {
        TODO("Not yet implemented")
    }

    companion object {
        private const val TAG = "CloudGitHubService"
    }


}