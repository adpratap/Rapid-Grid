package com.noreplypratap.data.source.cache

import android.graphics.Bitmap

object MemoryCache {

    private const val MAX_CACHE_SIZE = 10 // Adjust as needed

    private val cache: LinkedHashMap<String, Bitmap> =
        object : LinkedHashMap<String, Bitmap>(MAX_CACHE_SIZE, 0.75f, true) {
            override fun removeEldestEntry(eldest: MutableMap.MutableEntry<String, Bitmap>): Boolean {
                // Remove the eldest entry if cache size exceeds the maximum limit
                return size > MAX_CACHE_SIZE
            }
        }

    fun get(url: String): Bitmap? {
        synchronized(this) {
            return cache[url]
        }
    }

    fun put(url: String, bitmap: Bitmap) {
        synchronized(this) {
            cache[url] = bitmap
        }
    }

    fun clear() {
        synchronized(this) {
            cache.clear()
        }
    }
}
