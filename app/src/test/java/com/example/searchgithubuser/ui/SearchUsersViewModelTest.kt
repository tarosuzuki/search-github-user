package com.example.searchgithubuser.ui

import app.cash.turbine.test
import com.example.searchgithubuser.model.dispatcher.TestDefaultDispatcher
import com.example.searchgithubuser.model.github.FakeGitHubService
import com.example.searchgithubuser.model.github.GitHubRepositoryInfo
import com.example.searchgithubuser.model.github.GitHubUser
import com.example.searchgithubuser.model.github.GitHubUserInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import java.lang.IllegalStateException

@ExperimentalCoroutinesApi
class SearchUsersViewModelTest {
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var gitHubService: FakeGitHubService
    private lateinit var searchUsersViewModel: SearchUsersViewModel
    private val searchKeyword = "taro"
    private val userName = "taro-0"
    private val userList: List<GitHubUser> = listOf(
        GitHubUser(login = userName, avatar_url = "https://avatars.githubusercontent.com/u/65880?v=4")
    )
    private val userInfo = GitHubUserInfo(
        avatar_url = "https://avatars.githubusercontent.com/u/5248306?v=4",
        login = "taro-0",
        name = "Estuardo Díaz",
        followers = 52,
        following = 115
    )
    private val repositoryInfo = listOf(
        GitHubRepositoryInfo(
            name = "AboveBeneath",
            language = "CSS",
            stargazers_count = 0,
            description = "A landing page template with a featured content section and background sounds that change according to the view.",
            fork = false,
            html_url = "https://github.com/taro-0/AboveBeneath"
        )
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        gitHubService = FakeGitHubService()
        searchUsersViewModel = SearchUsersViewModel(
            gitHubService = gitHubService,
            defaultDispatcher = TestDefaultDispatcher(testDispatcher)
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun searchKeyword_startAsEmptyString() {
        assertEquals(true, searchUsersViewModel.searchKeyword.value.isEmpty())
    }

    @Test
    fun userList_startAsEmptyList() {
        assertEquals(true, searchUsersViewModel.userList.value.isEmpty())
    }

    @Test
    fun userInfo_startAsNull() {
        assertEquals(null, searchUsersViewModel.userInfo.value)
    }

    @Test
    fun repositoryList_startAsEmptyList() {
        assertEquals(true, searchUsersViewModel.repositoryList.value.isEmpty())
    }

    @Test
    fun isLoadingSearchResult_startAsFalse() {
        assertEquals(false, searchUsersViewModel.isLoadingSearchResult.value)
    }

    @Test
    fun isLoadingUserInfo_startAsFalse() {
        assertEquals(false, searchUsersViewModel.isLoadingUserInfo.value)
    }

    @Test
    fun isLoadingRepositoryInfo_startAsFalse() {
        assertEquals(false, searchUsersViewModel.isLoadingRepositoryInfo.value)
    }

    @Test
    fun isVisibleErrorModal_startAsFalse() {
        assertEquals(false, searchUsersViewModel.isVisibleErrorModal.value)
    }

    @Test
    fun setSearchKeyword_updateSearchKeyword() {
        val keyword1 = "hogehoge"
        val keyword2 = "fugafuga"

        searchUsersViewModel.setSearchKeyword(keyword1)
        assertEquals(keyword1, searchUsersViewModel.searchKeyword.value)
        searchUsersViewModel.setSearchKeyword(keyword2)
        assertEquals(keyword2, searchUsersViewModel.searchKeyword.value)
    }

    @Test
    fun searchUsers_whenSearchKeywordIsNotEmpty_invokesGitHubServiceSearchUsers() {
        gitHubService.searchUsersResults[searchKeyword] = Result.success(userList)

        searchUsersViewModel.setSearchKeyword(searchKeyword)
        searchUsersViewModel.searchUsers()

        assertEquals(listOf(searchKeyword), gitHubService.searchUsersInvocations)
    }

    @Test
    fun searchUsers_whenSearchKeywordIsEmpty_notInvokeGitHubServiceSearchUsers() {
        gitHubService.searchUsersResults[searchKeyword] = Result.success(userList)

        searchUsersViewModel.setSearchKeyword("")
        searchUsersViewModel.searchUsers()

        assertNotEquals(listOf(""), gitHubService.searchUsersInvocations)
    }

    @Test
    fun searchUsers_whenGitHubServiceSearchUsersReturnSuccess_updatesUserList() {
        gitHubService.searchUsersResults[searchKeyword] = Result.success(userList)

        searchUsersViewModel.setSearchKeyword(searchKeyword)
        searchUsersViewModel.searchUsers()

        assertEquals(userName, searchUsersViewModel.userList.value[0].login)
    }

    @Test
    fun searchUsers_whenGitHubServiceSearchUsersReturnFailure_notUpdatesUserList() {
        gitHubService.searchUsersResults[searchKeyword] = Result.failure(IllegalStateException("some error"))

        searchUsersViewModel.setSearchKeyword(searchKeyword)
        searchUsersViewModel.searchUsers()

        assertEquals(true, searchUsersViewModel.userList.value.isEmpty())
    }

    @Test
    fun searchUsers_whenUserListIsNotEmpty_clearsUserList() =
        runTest(testDispatcher) {
            gitHubService.searchUsersResults[searchKeyword] = Result.success(userList)

            searchUsersViewModel.setSearchKeyword(searchKeyword)
            searchUsersViewModel.searchUsers()

            searchUsersViewModel.userList.test {
                assertEquals(userList, awaitItem())
                searchUsersViewModel.searchUsers()
                assertEquals(listOf<GitHubUser>(), awaitItem())
                assertEquals(userList, awaitItem())
            }
        }

    // ToDo : verify stateFlow emissions with fast update
    // https://github.com/Kotlin/kotlinx.coroutines/issues/3143
    @Test
    fun searchUsers_setsIsLoadingSearchResult() =
        runTest(testDispatcher) {
            gitHubService.searchUsersResults[searchKeyword] = Result.success(userList)
            searchUsersViewModel.setSearchKeyword(searchKeyword)

            searchUsersViewModel.isLoadingSearchResult.test {
                assertEquals(false, awaitItem())
                searchUsersViewModel.searchUsers()
                advanceTimeBy(250)
                runCurrent()
                assertEquals(true, awaitItem())
                advanceTimeBy(300)
                runCurrent()
                assertEquals(false, awaitItem())
            }
        }

    @Test
    fun searchUsers_whenGitHubServiceSearchUsersReturnFailure_setsIsVisibleErrorModal() {
        gitHubService.searchUsersResults[searchKeyword] = Result.failure(IllegalStateException("some error"))

        searchUsersViewModel.setSearchKeyword(searchKeyword)
        searchUsersViewModel.searchUsers()

        assertEquals(true, searchUsersViewModel.isVisibleErrorModal.value)
    }

    @Test
    fun selectUser_invokesGitHubServiceGetUserInfoAndGetRepositoryInfo() {
        gitHubService.getUserInfoResults[userName] = Result.success(userInfo)
        gitHubService.getRepositoryInfoResults[userName] = Result.success(repositoryInfo)

        searchUsersViewModel.selectUser(userName)

        assertEquals(listOf(userName), gitHubService.getUserInfoInvocations)
        assertEquals(listOf(userName), gitHubService.getRepositoryInfoInvocations)
    }

    @Test
    fun selectUser_whenGitHubServiceGetUserInfoReturnsSuccess_updatesUserInfoAndRepositoryInfo() {
        gitHubService.getUserInfoResults[userName] = Result.success(userInfo)
        gitHubService.getRepositoryInfoResults[userName] = Result.success(repositoryInfo)

        searchUsersViewModel.selectUser(userName)

        assertEquals(userInfo, searchUsersViewModel.userInfo.value)
    }

    @Test
    fun selectUser_whenGitHubServiceGetUserInfoReturnsFailure_notUpdatesUserInfo() {
        gitHubService.getUserInfoResults[userName] = Result.failure(IllegalStateException("some error"))
        gitHubService.getRepositoryInfoResults[userName] = Result.success(repositoryInfo)

        searchUsersViewModel.selectUser(userName)

        assertEquals(null, searchUsersViewModel.userInfo.value)
    }

    @Test
    fun selectUser_whenGitHubServiceGetRepositoryInfoReturnsSuccess_updatesRepositoryInfo() {
        gitHubService.getUserInfoResults[userName] = Result.success(userInfo)
        gitHubService.getRepositoryInfoResults[userName] = Result.success(repositoryInfo)

        searchUsersViewModel.selectUser(userName)

        assertEquals(repositoryInfo, searchUsersViewModel.repositoryList.value)
    }

    @Test
    fun selectUser_whenGitHubServiceGetRepositoryInfoReturnsFailure_notUpdatesRepositoryInfo() {
        gitHubService.getUserInfoResults[userName] = Result.success(userInfo)
        gitHubService.getRepositoryInfoResults[userName] = Result.failure(IllegalStateException("some error"))

        searchUsersViewModel.selectUser(userName)

        assertEquals(true, searchUsersViewModel.repositoryList.value.isEmpty())
    }

    @Test
    fun selectUser_whenPreviousUserInfoIsSet_clearsPreviousUserInfo() =
        runTest(testDispatcher) {
            gitHubService.getUserInfoResults[userName] = Result.success(userInfo)
            gitHubService.getRepositoryInfoResults[userName] = Result.success(repositoryInfo)

            searchUsersViewModel.selectUser(userName)

            searchUsersViewModel.userInfo.test {
                assertEquals(userInfo, awaitItem())
                searchUsersViewModel.selectUser(userName)
                assertEquals(null, awaitItem())
                assertEquals(userInfo, awaitItem())
            }
        }

    @Test
    fun selectUser_whenPreviousRepositoryInfoIsSet_clearsPreviousRepositoryInfo() =
        runTest(testDispatcher) {
            gitHubService.getUserInfoResults[userName] = Result.success(userInfo)
            gitHubService.getRepositoryInfoResults[userName] = Result.success(repositoryInfo)

            searchUsersViewModel.selectUser(userName)

            searchUsersViewModel.repositoryList.test {
                assertEquals(repositoryInfo, awaitItem())
                searchUsersViewModel.selectUser(userName)
                assertEquals(listOf<GitHubRepositoryInfo>(), awaitItem())
                assertEquals(repositoryInfo, awaitItem())
            }
        }

    @Test
    fun selectUser_whenGitHubServiceGetUserInfoReturnFailure_setsIsVisibleErrorModal() {
        gitHubService.getUserInfoResults[userName] = Result.failure(IllegalStateException("some error"))
        gitHubService.getRepositoryInfoResults[userName] = Result.success(repositoryInfo)

        searchUsersViewModel.selectUser(userName)

        assertEquals(true, searchUsersViewModel.isVisibleErrorModal.value)
    }

    @Test
    fun selectUser_whenGitHubServiceGetRepositoryInfoReturnFailure_setsIsVisibleErrorModal() {
        gitHubService.getUserInfoResults[userName] = Result.success(userInfo)
        gitHubService.getRepositoryInfoResults[userName] = Result.failure(IllegalStateException("some error"))

        searchUsersViewModel.selectUser(userName)

        assertEquals(true, searchUsersViewModel.isVisibleErrorModal.value)
    }

    @Test
    fun selectUser_setsIsLoadingUserInfo() =
        runTest(testDispatcher) {
            gitHubService.getUserInfoResults[userName] = Result.success(userInfo)
            gitHubService.getRepositoryInfoResults[userName] = Result.success(repositoryInfo)

            searchUsersViewModel.isLoadingUserInfo.test {
                assertEquals(false, awaitItem())
                searchUsersViewModel.selectUser(userName)
                advanceTimeBy(250)
                runCurrent()
                assertEquals(true, awaitItem())
                advanceTimeBy(300)
                runCurrent()
                assertEquals(false, awaitItem())
            }
        }

    @Test
    fun selectUser_setsIsLoadingRepositoryInfo() =
        runTest(testDispatcher) {
            gitHubService.getUserInfoResults[userName] = Result.success(userInfo)
            gitHubService.getRepositoryInfoResults[userName] = Result.success(repositoryInfo)

            searchUsersViewModel.isLoadingRepositoryInfo.test {
                assertEquals(false, awaitItem())
                searchUsersViewModel.selectUser(userName)
                advanceTimeBy(250)
                runCurrent()
                assertEquals(true, awaitItem())
                advanceTimeBy(300)
                runCurrent()
                assertEquals(false, awaitItem())
            }
        }

    @Test
    fun setIsVisibleErrorModal_updateValue() {
        searchUsersViewModel.setIsVisibleErrorModal(true)
        assertEquals(true, searchUsersViewModel.isVisibleErrorModal.value)
        searchUsersViewModel.setIsVisibleErrorModal(false)
        assertEquals(false, searchUsersViewModel.isVisibleErrorModal.value)
    }
}
