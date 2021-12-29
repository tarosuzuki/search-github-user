package com.example.searchgithubuser.di

import com.example.searchgithubuser.model.github.CloudGitHubService
import com.example.searchgithubuser.model.github.FakeGitHubService
import com.example.searchgithubuser.model.github.GitHubService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Provides
    fun provideGitHubService() : GitHubService {
        //return FakeGitHubService()
        return CloudGitHubService()
    }
}