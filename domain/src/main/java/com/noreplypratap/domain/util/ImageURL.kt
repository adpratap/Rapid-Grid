package com.noreplypratap.domain.util

import com.noreplypratap.domain.model.ArticleDomain


fun ArticleDomain.toImageURL(): String {
    return "${thumbnail.domain}/${thumbnail.basePath}/0/${thumbnail.key}"
}