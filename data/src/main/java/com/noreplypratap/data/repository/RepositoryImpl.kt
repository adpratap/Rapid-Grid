package com.noreplypratap.data.repository

import com.noreplypratap.data.mapper.toArticle
import com.noreplypratap.data.mapper.toImageURL
import com.noreplypratap.data.source.local.ArticleDao
import com.noreplypratap.data.source.remote.RemoteService
import com.noreplypratap.domain.model.ArticleDomain
import com.noreplypratap.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Response

class RepositoryImpl(
    private val articleDao: ArticleDao,
    private val remoteService: RemoteService
): Repository {

    override suspend fun createArticle(articleDomain: List<ArticleDomain>) {
        articleDao.createArticle(articleDomain.map { it.toArticle() })
    }

    override fun readArticles(): Flow<List<String>> = articleDao.readArticles().map {
        it.map { article ->
            article.toImageURL()
        }
    }
    override suspend fun updateArticle(articleDomain: ArticleDomain) {
        articleDao.updateArticle(articleDomain.toArticle())
    }

    override suspend fun deleteArticle(articleDomain: ArticleDomain) {
        articleDao.deleteArticle(articleDomain.toArticle())
    }

    override suspend fun deleteEverything() {
        articleDao.deleteEverything()
    }

    override suspend fun getArticles(limit: Int): Response<List<ArticleDomain>> {
        return remoteService.getArticles(limit)
    }
}