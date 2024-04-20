package com.noreplypratap.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.noreplypratap.data.model.Article

@Database(
    entities = [Article::class],
    version = 1,
    exportSchema = false
)
abstract class ArticleDatabase : RoomDatabase() {
    abstract val articleDao : ArticleDao
    companion object{
        const val DATABASE_NAME = "article_db"
    }
}