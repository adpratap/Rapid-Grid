package com.noreplypratap.data.mapper

import com.noreplypratap.data.model.Article
import com.noreplypratap.domain.model.ArticleDomain

fun ArticleDomain.toArticle(): Article {
    return Article(
        basePath = thumbnail.basePath,
        domain = thumbnail.domain,
        key = thumbnail.key,
        id = id,
        language = language,
        title = title
    )
}

fun Article.toImageURL(): String {
    return "$domain/$basePath/0/$key"
}

fun ArticleDomain.toImageURL(): String {
    return "${thumbnail.domain}/${thumbnail.basePath}/0/${thumbnail.key}"
}