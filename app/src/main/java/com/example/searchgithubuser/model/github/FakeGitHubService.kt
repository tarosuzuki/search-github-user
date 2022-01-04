package com.example.searchgithubuser.model.github

import java.lang.IllegalStateException

/** Fake module of [GitHubService]. */
class FakeGitHubService : GitHubService {
    /** Invocations of [searchUsers] */
    var searchUsersInvocations: MutableList<String> = mutableListOf()
    /** Results of invocations of [searchUsers], represented as map of input parameter to returned value */
    var searchUsersResults: MutableMap<String, Result<List<GitHubUser>>> = mutableMapOf()

    /** Invocations of [getUserInfo] */
    var getUserInfoInvocations: MutableList<String> = mutableListOf()
    /** Results of invocations of [getUserInfo], represented as map of input parameter to returned value */
    var getUserInfoResults: MutableMap<String, Result<GitHubUserInfo>> = mutableMapOf()

    /** Invocations of [getRepositoryInfo] */
    var getRepositoryInfoInvocations: MutableList<String> = mutableListOf()
    /** Results of invocations of [getRepositoryInfo], represented as map of input parameter to returned value */
    var getRepositoryInfoResults: MutableMap<String, Result<List<GitHubRepositoryInfo>>> = mutableMapOf()

    init {
        val fakeDefaultSearchUsersResponse = listOf(
            GitHubUser(login = "taro", avatar_url = "https://avatars.githubusercontent.com/u/65880?v=4"),
            GitHubUser(login = "taroxd", avatar_url = "https://avatars.githubusercontent.com/u/6070540?v=4"),
            GitHubUser(login = "taro-0", avatar_url = "https://avatars.githubusercontent.com/u/5248306?v=4")
        )
        searchUsersResults["taro"] = Result.success(fakeDefaultSearchUsersResponse)

        val fakeDefaultGetUserInfoResponse = GitHubUserInfo(
            avatar_url = "https://avatars.githubusercontent.com/u/5248306?v=4",
            login = "taro-0",
            name = "Estuardo Díaz",
            followers = 52,
            following = 115
        )
        getUserInfoResults["taro-0"] = Result.success(fakeDefaultGetUserInfoResponse)

        val fakeDefaultGetUserRepositoryInfoResponse = listOf(
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
        getRepositoryInfoResults["taro-0"] = Result.success(fakeDefaultGetUserRepositoryInfoResponse)
    }

    override suspend fun searchUsers(keyword: String): Result<List<GitHubUser>> {
        searchUsersInvocations.add(keyword)
        return searchUsersResults[keyword]
            ?: Result.failure(IllegalStateException("No result for request: $keyword"))
    }

    override suspend fun getUserInfo(userName: String): Result<GitHubUserInfo> {
        getUserInfoInvocations.add(userName)
        return getUserInfoResults[userName]
            ?: Result.failure(IllegalStateException("No result for request: $userName"))
    }

    override suspend fun getRepositoryInfo(userName: String): Result<List<GitHubRepositoryInfo>> {
        getRepositoryInfoInvocations.add(userName)
        return getRepositoryInfoResults[userName]
            ?: Result.failure(IllegalStateException("No result for request: $userName"))
    }
}
