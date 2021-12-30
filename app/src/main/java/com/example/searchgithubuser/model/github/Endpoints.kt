package com.example.searchgithubuser.model.github

import com.example.searchgithubuser.BuildConfig

val gitHubEndpoint: String
    get() = if (BuildConfig.DEBUG) { "https://api.github.com/" } else { "https://api.github.com/" }
