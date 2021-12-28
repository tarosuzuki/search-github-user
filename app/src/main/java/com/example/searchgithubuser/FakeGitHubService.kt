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
        val fakeResponse = GitHubUserInfo(
            avatar_url = "https://avatars.githubusercontent.com/u/5248306?v=4",
            login = "taro-0",
            name = "Estuardo Díaz",
            followers = 52,
            following = 115
        )
        return Result.success(fakeResponse)
    }

    override suspend fun getRepositoryInfo (userName: String) : Result<List<GitHubRepositoryInfo>> {
        val fakeResponse = listOf(
            GitHubRepositoryInfo(
                name = "AboveBeneath",
                language = "CSS",
                stargazers_count = 0,
                description = "A landing page template with a featured content section and background sounds that change according to the view.",
                fork = false,
                html_url = "https://github.com/taro-0/AboveBeneath"
            ),
            GitHubRepositoryInfo(
                name = "AboveBeneath",
                language = "CSS",
                stargazers_count = 0,
                description = "A landing page template with a featured content section and background sounds that change according to the view.",
                fork = false,
                html_url = "https://github.com/taro-0/AboveBeneath"
            ),
            GitHubRepositoryInfo(
                name = "AboveBeneath",
                language = "CSS",
                stargazers_count = 0,
                description = "A landing page template with a featured content section and background sounds that change according to the view.",
                fork = false,
                html_url = "https://github.com/taro-0/AboveBeneath"
            ),
        )
        return Result.success(fakeResponse)
    }

    companion object {
        private const val TAG = "FakeGitHubService"
    }


}