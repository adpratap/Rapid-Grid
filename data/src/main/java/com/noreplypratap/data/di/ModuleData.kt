package com.noreplypratap.data.di

import android.app.Application
import androidx.room.Room
import com.noreplypratap.data.repository.RepositoryImpl
import com.noreplypratap.data.source.local.ArticleDao
import com.noreplypratap.data.source.local.ArticleDatabase
import com.noreplypratap.data.source.remote.RemoteConstants
import com.noreplypratap.data.source.remote.RemoteService
import com.noreplypratap.domain.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ModuleData {

    @Provides
    @Singleton
    fun provideRemoteRepository(remoteService: RemoteService, articleDao: ArticleDao) : Repository {
        return RepositoryImpl(articleDao,remoteService)
    }

    @Provides
    @Singleton
    fun provideNewsService(retrofit: Retrofit): RemoteService {
        return retrofit.create(RemoteService::class.java)
    }

    @Provides
    @Singleton
    fun provideRetrofit(@BASEURl baseURL: String, okHttpClient: OkHttpClient ): Retrofit {
        return Retrofit.Builder().baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create()).client(okHttpClient).build()
    }

    @Provides
    @Singleton
    fun provideClient() : OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
    }

    @Provides
    @Singleton
    fun provideDatabase(context: Application) : ArticleDatabase {
        return Room.databaseBuilder(
            context,
            ArticleDatabase::class.java,
            ArticleDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideNewsDao(databaseArticles: ArticleDatabase) : ArticleDao {
        return databaseArticles.articleDao
    }

    @Provides
    @BASEURl
    fun provideBaseURL(): String {
        return RemoteConstants.BASE_URL
    }

}
