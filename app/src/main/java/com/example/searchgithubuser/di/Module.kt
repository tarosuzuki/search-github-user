package com.example.searchgithubuser.di

import com.example.searchgithubuser.model.dispatcher.DefaultDispatcher
import com.example.searchgithubuser.model.dispatcher.DefaultDispatcherImpl
import com.example.searchgithubuser.model.github.CloudGitHubService
import com.example.searchgithubuser.model.github.GitHubService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Provides
    fun provideGitHubService(): GitHubService {
        // return FakeGitHubService()
        return CloudGitHubService()
    }

    @Provides
    fun provideDefaultDispatcher(): DefaultDispatcher {
        return DefaultDispatcherImpl()
    }
}
