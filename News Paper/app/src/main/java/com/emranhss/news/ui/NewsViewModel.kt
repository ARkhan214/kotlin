package com.emranhss.news.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.emranhss.news.NewsApplication
import com.emranhss.news.models.Article
import com.emranhss.news.models.NewsResponse
import com.emranhss.news.repository.NewsRepository
import com.emranhss.news.util.Constants
import com.emranhss.news.util.Resource
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response

class NewsViewModel(
    app: Application,
    val newsRepository: NewsRepository
) : AndroidViewModel(app) {

    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1

    var breakingNewsResponse: NewsResponse? = null

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1

    var searchNewsResponse: NewsResponse? = null

    // LiveData to hold message after save attempt
    private val _saveArticleMessage = MutableLiveData<String>()
    val saveArticleMessage = _saveArticleMessage


    init {
        getBreakingNews(countryCode = "us")
    }

    // -------------------- Breaking News --------------------
    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        safeBreakingNewsCall(countryCode)

    }

    // -------------------- Search News --------------------
    fun searchNews(query: String) = viewModelScope.launch {
        safeSearchNewsCall(query)

    }

    // Add a new var to track last page for search
    var isLastPageSearch = false


    private fun handelBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                Log.d(
                    "PAGINATION",
                    "Page loaded: $breakingNewsPage, items: ${resultResponse.articles.size}"
                )
                breakingNewsPage++
                if (breakingNewsResponse == null) {
                    breakingNewsResponse = resultResponse
                } else {
                    val oldArticle = breakingNewsResponse?.articles
                    val newArticle = resultResponse.articles
                    oldArticle?.addAll(newArticle)
                }

                return Resource.Success(breakingNewsResponse ?: resultResponse)
            }
        }
        // ------------------------------
        // ERROR HANDLING: Parse JSON body to get the "message" from API
        // response.message() is not reliable for API errors like "rateLimited"
        val errorMessage = try {
            val json = response.errorBody()?.string()
            if (!json.isNullOrEmpty()) {
                org.json.JSONObject(json).getString("message") // Extract "message" from JSON
            } else response.message()
        } catch (e: Exception) {
            "Unknown error"
        }
        return Resource.Error(errorMessage)
//        return Resource.Error(response.message())
    }

    private fun handelSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                searchNewsPage++
                if (searchNewsResponse == null) {
                    searchNewsResponse = resultResponse
                } else {
                    val oldArticle = searchNewsResponse?.articles
                    val newArticle = resultResponse.articles
                    oldArticle?.addAll(newArticle)
                }

                return Resource.Success(searchNewsResponse ?: resultResponse)
            }
        }

        // ------------------------------
        // ERROR HANDLING: Parse JSON body to get the "message" from API
        val errorMessage = try {
            val json = response.errorBody()?.string()
            if (!json.isNullOrEmpty()) {
                org.json.JSONObject(json).getString("message") // Extract "message" from JSON
            } else response.message()
        } catch (e: Exception) {
            "Unknown error"
        }

        return Resource.Error(errorMessage)
    }

    fun saveArticle( article: Article,onResult: (String) -> Unit) = viewModelScope.launch {
        val message = newsRepository.upsertArticle(article)
        onResult(message)
    }

    fun getSaveArticle() = newsRepository.getAllArticles()


    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }

    private suspend fun safeSearchNewsCall(searchQuery : String) {
        searchNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = newsRepository.searchNews(searchQuery, searchNewsPage)
                searchNews.postValue(handelSearchNewsResponse(response))
            } else {
                searchNews.postValue(Resource.Error("No Internet Connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> searchNews.postValue(Resource.Error("Network Failure"))
                else -> searchNews.postValue(Resource.Error("Conversion Error"))
            }
        }
    }



    private suspend fun safeBreakingNewsCall(countryCode: String) {
        breakingNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = newsRepository.getBreakingNews(countryCode, breakingNewsPage)
                breakingNews.postValue(handelBreakingNewsResponse(response))
            } else {
                breakingNews.postValue(Resource.Error("No Internet Connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> breakingNews.postValue(Resource.Error("Network Failure"))
                else -> breakingNews.postValue(Resource.Error("Conversion Error"))
            }
        }
    }


    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<NewsApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }


}