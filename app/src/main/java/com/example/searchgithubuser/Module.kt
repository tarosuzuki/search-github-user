package com.example.searchgithubuser

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Provides
    fun provideGitHubService() : GitHubService {
        return FakeGitHubService()
        //return CloudGitHubService()
    }
}