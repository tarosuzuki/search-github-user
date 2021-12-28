package com.example.searchgithubuser

class FakeGitHubService : GitHubService {
    override suspend fun getUsers(keyword: String): Result<List<GitHubUser>> {
        val fakeResponse = listOf(
            GitHubUser(login = "taro", avatar_url = "https://avatars.githubusercontent.com/u/65880?v=4"),
            GitHubUser(login = "taroxd", avatar_url = "https://avatars.githubusercontent.com/u/6070540?v=4"),
            GitHubUser(login = "taro-0", avatar_url = "https://avatars.githubusercontent.com/u/5248306?v=4")
        )
        return Result.success(fakeResponse)
    }

    override suspend fun getUserInfo(userName: String): Result<GitHubUserInfo> {
        TODO("Not yet implemented")
    }

    override suspend fun getRepositoryInfo (userName: String) : Result<List<GitHubRepositoryInfo>> {
        TODO("Not yet implemented")
    }

    companion object {
        private const val TAG = "FakeGitHubService"
    }


}