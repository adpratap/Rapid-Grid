package com.noreplypratap.domain.repository


import com.noreplypratap.domain.model.ArticleDomain
import kotlinx.coroutines.flow.Flow
import retrofit2.Response


interface Repository {

    // API Call
    suspend fun getArticles(
        limit: Int = 100
    ): Response<List<ArticleDomain>>

    // Local DB
    suspend fun createArticle(articleDomain: List<ArticleDomain>)
    fun readArticles(): Flow<List<String>>
    suspend fun updateArticle(articleDomain: ArticleDomain)
    suspend fun deleteArticle(articleDomain: ArticleDomain)
    suspend fun deleteEverything()

}