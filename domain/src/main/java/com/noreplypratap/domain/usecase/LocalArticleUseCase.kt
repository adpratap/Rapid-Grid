package com.noreplypratap.domain.usecase

import com.noreplypratap.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalArticleUseCase @Inject constructor(
    private val repository: Repository
) {
    operator fun invoke(): Flow<List<String>> = repository.readArticles()
}