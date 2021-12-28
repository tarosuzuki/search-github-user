package com.example.searchgithubuser

import androidx.hilt.lifecycle.ViewModelInject
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
    private var fetchUsersJob: Job? = null

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
}