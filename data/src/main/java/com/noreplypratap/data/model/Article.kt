package com.noreplypratap.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articles")
data class Article(
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0,
    val id: String,
    val language: String,
    val title: String,
    val basePath: String,
    val domain: String,
    val key: String
)