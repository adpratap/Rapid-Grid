package com.noreplypratap.data.source.cache

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object DiskCache {
    private const val CACHE_DIRECTORY = "image_cache"

    fun saveBitmap(context: Context, url: String, bitmap: Bitmap) {
        val cacheDir = File(context.cacheDir, CACHE_DIRECTORY)
        if (!cacheDir.exists()) {
            cacheDir.mkdirs()
        }
        val file = File(cacheDir, getFileName(url))
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 20, fos)
            fos.flush()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            fos?.close()
        }
    }

    fun loadBitmap(context: Context, url: String): Bitmap? {
        val cacheDir = File(context.cacheDir, CACHE_DIRECTORY)
        val file = File(cacheDir, getFileName(url))
        return if (file.exists()) {
            BitmapFactory.decodeFile(file.absolutePath)
        } else {
            null
        }
    }

    private fun getFileName(url: String): String {
        return url.hashCode().toString()
    }
}
