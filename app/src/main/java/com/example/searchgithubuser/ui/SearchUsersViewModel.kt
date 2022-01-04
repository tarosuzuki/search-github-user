package com.example.searchgithubuser.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchgithubuser.model.dispatcher.DefaultDispatcher
import com.example.searchgithubuser.model.github.GitHubRepositoryInfo
import com.example.searchgithubuser.model.github.GitHubService
import com.example.searchgithubuser.model.github.GitHubUser
import com.example.searchgithubuser.model.github.GitHubUserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchUsersViewModel @Inject constructor(
    private val gitHubService: GitHubService,
    private val defaultDispatcher: DefaultDispatcher
) : ViewModel() {

    private val _searchKeyword = MutableStateFlow("")
    val searchKeyword: StateFlow<String> = _searchKeyword
    private val _userList = MutableStateFlow<List<GitHubUser>>(listOf())
    val userList: StateFlow<List<GitHubUser>> = _userList
    private val _userInfo = MutableStateFlow<GitHubUserInfo?>(null)
    val userInfo: StateFlow<GitHubUserInfo?> = _userInfo
    private val _repositoryList = MutableStateFlow<List<GitHubRepositoryInfo>>(listOf())
    val repositoryList: StateFlow<List<GitHubRepositoryInfo>> = _repositoryList
    private val _isLoadingSearchResult = MutableStateFlow<Boolean>(false)
    val isLoadingSearchResult: StateFlow<Boolean> = _isLoadingSearchResult
    private val _isLoadingUserInfo = MutableStateFlow<Boolean>(false)
    val isLoadingUserInfo: StateFlow<Boolean> = _isLoadingUserInfo
    private val _isLoadingRepositoryInfo = MutableStateFlow<Boolean>(false)
    val isLoadingRepositoryInfo: StateFlow<Boolean> = _isLoadingRepositoryInfo
    private var fetchUsersJob: Job? = null
    private var fetchUserInfoJob: Job? = null
    private var fetchRepositoriesJob: Job? = null

    private fun fetchUserList(keyword: String) {
        fetchUsersJob?.cancel()
        fetchUsersJob = viewModelScope.launch(defaultDispatcher.dispatcher) {
            setIsLoadingSearchResult(true)
            val result = gitHubService.searchUsers(keyword)
            setIsLoadingSearchResult(false)
            if (result.isSuccess) {
                result.getOrNull()?.let {
                    _userList.value = it
                }
            }
        }
    }

    private fun clearUserList() {
        _userList.value = listOf()
    }

    private fun fetchUserInfo(userName: String) {
        fetchUserInfoJob?.cancel()
        fetchUserInfoJob = viewModelScope.launch(defaultDispatcher.dispatcher) {
            setIsLoadingUserInfo(true)
            val result = gitHubService.getUserInfo(userName)
            setIsLoadingUserInfo(false)
            if (result.isSuccess) {
                result.getOrNull()?.let {
                    _userInfo.value = it
                }
            }
        }
    }

    private fun clearUserInfo() {
        _userInfo.value = null
    }

    private fun fetchRepositoryList(userName: String) {
        fetchRepositoriesJob?.cancel()
        fetchRepositoriesJob = viewModelScope.launch(defaultDispatcher.dispatcher) {
            setIsLoadingRepositoryInfo(true)
            val result = gitHubService.getRepositoryInfo(userName)
            setIsLoadingRepositoryInfo(false)
            if (result.isSuccess) {
                result.getOrNull()?.let {
                    _repositoryList.value = it
                }
            }
        }
    }

    private fun clearRepositoryList() {
        _repositoryList.value = listOf()
    }

    private fun setIsLoadingSearchResult(value: Boolean) {
        _isLoadingSearchResult.value = value
    }

    private fun setIsLoadingUserInfo(value: Boolean) {
        _isLoadingUserInfo.value = value
    }

    private fun setIsLoadingRepositoryInfo(value: Boolean) {
        _isLoadingRepositoryInfo.value = value
    }

    fun setSearchKeyword(keyword: String) {
        _searchKeyword.value = keyword
    }

    fun searchUsers() {
        val userName = _searchKeyword.value
        clearUserList()
        if (userName.isNotEmpty()) {
            fetchUserList(userName)
        }
    }

    fun selectUser(userName: String) {
        clearUserInfo()
        clearRepositoryList()
        fetchUserInfo(userName)
        fetchRepositoryList(userName)
    }
}
