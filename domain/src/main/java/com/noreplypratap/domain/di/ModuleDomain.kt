package com.noreplypratap.domain.di

import com.noreplypratap.domain.repository.Repository
import com.noreplypratap.domain.usecase.LocalArticleUseCase
import com.noreplypratap.domain.usecase.RemoteArticleUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ModuleDomain {

    @Provides
    @Singleton
    fun provideRemoteArticleUseCase(repository: Repository): RemoteArticleUseCase {
        return RemoteArticleUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideLocalArticleUseCase(repository: Repository): LocalArticleUseCase {
        return LocalArticleUseCase(repository)
    }

}