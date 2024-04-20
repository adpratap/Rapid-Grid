package com.noreplypratap.domain.usecase

import com.noreplypratap.domain.util.toImageURL
import com.noreplypratap.domain.repository.Repository
import com.noreplypratap.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class RemoteArticleUseCase @Inject constructor(
    private val repository: Repository
) {
    operator fun invoke(
        limit: Int = 100
    ): Flow<Resource<List<String>>> =
        flow {
            emit(Resource.Loading())
            try {
                val response = repository.getArticles(limit)
                if (response.isSuccessful){
                    response.body()?.let { repository.createArticle(it) }
                    val iURL = response.body()?.map { it.toImageURL() }
                    if (iURL != null) {
                        emit(Resource.Success(iURL))
                    }
                }
            } catch (e: Exception) {
                emit(Resource.Error("Exception"))
            } catch (e: HttpException) {
                emit(Resource.Error("HttpException"))
            }
        }
}