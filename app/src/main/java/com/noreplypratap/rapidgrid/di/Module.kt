package com.noreplypratap.rapidgrid.di

import android.content.Context
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@dagger.Module
@InstallIn(SingletonComponent::class)
object Module {

    @Singleton
    @Provides
    fun provideContext(@ApplicationContext application: Context): Context {
        return application
    }

}