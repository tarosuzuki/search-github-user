package com.example.searchgithubuser.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.example.searchgithubuser.model.github.FakeGitHubService
import com.example.searchgithubuser.model.github.GitHubUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SearchUsersScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()
    private lateinit var gitHubService: FakeGitHubService
    private lateinit var viewModel: SearchUsersViewModel
    private var onClickedUserList = false
    private var selectedUserName: String? = null
    private val searchKeyword = "taro"
    private val userName1 = "taro"
    private val userName2 = "taroxd"
    private val userName3 = "taro-0"
    private val userList: List<GitHubUser> = listOf(
        GitHubUser(login = "taro", avatar_url = "https://avatars.githubusercontent.com/u/65880?v=4"),
        GitHubUser(login = "taroxd", avatar_url = "https://avatars.githubusercontent.com/u/6070540?v=4"),
        GitHubUser(login = "taro-0", avatar_url = "https://avatars.githubusercontent.com/u/5248306?v=4")
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        gitHubService = FakeGitHubService()
        viewModel = SearchUsersViewModel(gitHubService)
        onClickedUserList = false
        selectedUserName = null

        composeTestRule.mainClock.autoAdvance = false
        composeTestRule.waitForIdle()
        composeTestRule.setContent {
            SearchUsersScreen(
                viewModel = viewModel,
                onClickUserList = { userName ->
                    onClickedUserList = true
                    selectedUserName = userName
                }
            )
        }
        composeTestRule.mainClock.advanceTimeBy(500)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun onLoad_displaysDefaults() {
        composeTestRule.onNodeWithTag(InputTextFiledForKeywordSearchTag)
            .assertExists("Unable to find input keyword box")
    }

    @Test
    fun userInfo_whenUserListIsUpdated_isShown() {
        gitHubService.searchUsersResults[searchKeyword] = Result.success(userList)

        composeTestRule.onNodeWithTag("$SearchUserResultTag-$userName1")
            .assertDoesNotExist()
        composeTestRule.onNodeWithTag("$SearchUserResultTag-$userName2")
            .assertDoesNotExist()
        composeTestRule.onNodeWithTag("$SearchUserResultTag-$userName3")
            .assertDoesNotExist()

        viewModel.setSearchKeyword(searchKeyword)
        viewModel.searchUsers()
        composeTestRule.mainClock.advanceTimeBy(500)

        composeTestRule.onNodeWithTag("$SearchUserResultTag-$userName1")
            .assertExists("Unable to find user info")
        composeTestRule.onNodeWithTag("$SearchUserResultTag-$userName2")
            .assertExists("Unable to find user info")
        composeTestRule.onNodeWithTag("$SearchUserResultTag-$userName3")
            .assertExists("Unable to find user info")
    }

    @Test
    fun userInfoColumn_whenClicked_invokesOnClickUserList() {
        gitHubService.searchUsersResults[searchKeyword] = Result.success(userList)
        viewModel.setSearchKeyword(searchKeyword)
        viewModel.searchUsers()
        composeTestRule.mainClock.advanceTimeBy(500)
        composeTestRule.onNodeWithTag("$SearchUserResultTag-$userName1")
            .assertExists("Unable to find user info")
        composeTestRule.onNodeWithTag("$SearchUserResultTag-$userName1")
            .performClick()
        composeTestRule.mainClock.advanceTimeBy(500)

        assertEquals(true, onClickedUserList)
        assertEquals(userName1, selectedUserName)
    }
}
