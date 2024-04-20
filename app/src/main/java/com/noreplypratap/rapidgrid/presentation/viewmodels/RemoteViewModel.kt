package com.noreplypratap.rapidgrid.presentation.viewmodels


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noreplypratap.data.source.cache.DiskCache
import com.noreplypratap.data.source.cache.MemoryCache
import com.noreplypratap.domain.usecase.LocalArticleUseCase
import com.noreplypratap.domain.usecase.RemoteArticleUseCase
import com.noreplypratap.domain.util.Resource
import com.noreplypratap.rapidgrid.model.ImageData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

@HiltViewModel
class RemoteViewModel @Inject constructor(
    private val context: Context,
    private val remoteArticleUseCase: RemoteArticleUseCase,
    private val localArticleUseCase: LocalArticleUseCase
): ViewModel() {

    private val _images =  MutableLiveData<List<ImageData>>()
    val images: LiveData<List<ImageData>> get() = _images

    init {
        remoteArticle()
    }

    private fun remoteArticle() = viewModelScope.launch(Dispatchers.IO) {
        remoteArticleUseCase().collect { articleDomain ->
            when (articleDomain) {
                is Resource.Success -> {
                    articleDomain.data?.let {
                        _images.postValue(forImage(it))
                    }
                }
                is Resource.Error -> {
                    localArticleUseCase().collect {
                        _images.postValue(forImage(it))
                    }
                }
                is Resource.Loading -> {
                    //Loading ...
                }
            }
        }
    }

    private fun forImage(list: List<String>): List<ImageData> {
        return list.map { it.toImageData() }
    }

    private fun String.toImageData(): ImageData {
        return ImageData(this, null)
    }

    fun loadVisibleImage(firstVisibleItemPosition: Int, lastVisibleItemPosition: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = _images.value ?: return@launch
            for (pos in firstVisibleItemPosition..lastVisibleItemPosition) {
                if (list.getOrNull(pos)?.bitmap == null) {
                    val imageURL = list[pos].imageURL
                    val bitmap = MemoryCache.get(imageURL) ?: DiskCache.loadBitmap(context, imageURL)
                    if (bitmap != null) {
                        // Update memory cache with bitmap retrieved from disk
                        MemoryCache.put(imageURL, bitmap)
                        list[pos].bitmap = bitmap
                        _images.postValue(list)
                    } else {
                        // Load image from network if not found in caches
                        loadImage(list,pos)
                    }
                }
            }
        }
    }

    private fun loadImage(list: List<ImageData>, pos: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val url = list[pos].imageURL
                val bitmap = downloadImage(url)
                if (bitmap != null) {
                    DiskCache.saveBitmap(context,url, bitmap)
                    MemoryCache.put(url, bitmap)
                    list[pos].bitmap = bitmap
                    _images.postValue(list)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun downloadImage(url: String): Bitmap? {
        val connection = URL(url).openConnection() as? HttpURLConnection
        connection?.connectTimeout = 3000 // Adjust timeout as needed
        connection?.readTimeout = 3000 // Adjust timeout as needed
        return try {
            if (connection?.responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream
                BitmapFactory.decodeStream(inputStream)
            } else {
                null // Return null if response code is not HTTP_OK
            }
        } catch (e: Exception) {
            e.printStackTrace() // Log the exception for debugging
            null // Return null if an exception occurs
        } finally {
            connection?.disconnect()
        }
    }
}
