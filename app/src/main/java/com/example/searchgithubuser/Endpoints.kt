package com.example.searchgithubuser

val gitHubEndpoint: String
    get() = if (BuildConfig.DEBUG) { "https://api.github.com/" } else { "https://api.github.com/" }