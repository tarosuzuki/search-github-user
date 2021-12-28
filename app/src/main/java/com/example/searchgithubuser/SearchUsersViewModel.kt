package com.example.searchgithubuser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchUsersViewModel @Inject constructor(
    private val gitHubService : GitHubService
) : ViewModel() {

    private val _userList = MutableStateFlow<List<GitHubUser>>(listOf())
    val userList: StateFlow<List<GitHubUser>> = _userList
    private val _userInfo = MutableStateFlow<GitHubUserInfo?>(null)
    val userInfo: StateFlow<GitHubUserInfo?> = _userInfo
    private val _repositoryList = MutableStateFlow<List<GitHubRepositoryInfo>>(listOf())
    val repositoryList: StateFlow<List<GitHubRepositoryInfo>> = _repositoryList
    private var fetchUsersJob: Job? = null
    private var fetchUserInfoJob: Job? = null
    private var fetchRepositoriesJob: Job? = null

    init {
        fetchUserInfo("taro-0")
        fetchRepositoryList("taro-0")
    }

    fun fetchUserList(keyword: String) {
        val searchQuery = "$keyword in:login"
        fetchUsersJob?.cancel()
        fetchUsersJob = viewModelScope.launch {
            val result = gitHubService.getUsers(searchQuery)
            if (result.isSuccess) {
                result.getOrNull()?.let {
                    _userList.value = it
                }
            }
        }
    }

    fun clearUserList() {
        _userList.value = listOf()
    }

    fun fetchUserInfo(userName: String) {
        fetchUserInfoJob?.cancel()
        fetchUserInfoJob = viewModelScope.launch {
            val result = gitHubService.getUserInfo(userName)
            if (result.isSuccess) {
                result.getOrNull()?.let {
                    _userInfo.value = it
                }
            }
        }
    }

    fun clearUserInfo() {
        _userInfo.value = null
    }

    fun fetchRepositoryList(userName: String) {
        fetchRepositoriesJob?.cancel()
        fetchRepositoriesJob = viewModelScope.launch {
            val result = gitHubService.getRepositoryInfo(userName)
            if (result.isSuccess) {
                result.getOrNull()?.let {
                    _repositoryList.value = it
                }
            }
        }
    }

    fun clearRepositoryList() {
        _repositoryList.value = listOf()
    }
}