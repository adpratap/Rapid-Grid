package com.noreplypratap.data.source.remote

import com.noreplypratap.domain.model.ArticleDomain
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RemoteService {

    @GET("/api/v2/content/misc/media-coverages")
    suspend fun getArticles(
        @Query("limit")
        limit : Int = RemoteConstants.LIMIT
    ): Response<List<ArticleDomain>>

}