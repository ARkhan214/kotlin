package com.emranhss.news.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emranhss.news.models.Article
import com.emranhss.news.models.NewsResponse
import com.emranhss.news.repository.NewsRepository
import com.emranhss.news.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(val newsRepository: NewsRepository): ViewModel() {

    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1

    var breakingNewsResponse: NewsResponse? = null

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1

    var searchNewsResponse: NewsResponse? = null


    init {
        getBreakingNews(countryCode = "us")
    }

    // -------------------- Breaking News --------------------
    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        Log.d("PAGINATION", "Calling page: $breakingNewsPage")
        breakingNews.postValue(Resource.Loading())
        try {
            val response = newsRepository.getBreakingNews(countryCode, breakingNewsPage)
            breakingNews.postValue(handelBreakingNewsResponse(response))
        } catch (e: Exception) {
            breakingNews.postValue(Resource.Error("Exception: ${e.localizedMessage}"))
        }
    }

    // -------------------- Search News --------------------
    fun searchNews(query: String) = viewModelScope.launch {
        searchNews.postValue(Resource.Loading())
        try {
            val response = newsRepository.searchNews(query, searchNewsPage)
            searchNews.postValue(handelSearchNewsResponse(response))
        } catch (e: Exception) {
            searchNews.postValue(Resource.Error("Exception: ${e.localizedMessage}"))
        }
    }

    private fun handelBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse>{
       if(response.isSuccessful){
           response.body()?.let { resultResponse ->
               Log.d(
                   "PAGINATION",
                   "Page loaded: $breakingNewsPage, items: ${resultResponse.articles.size}"
               )
               breakingNewsPage++
               if (breakingNewsResponse == null){
                   breakingNewsResponse = resultResponse
               }else{
                   val oldArticle = breakingNewsResponse?.articles
                   val newArticle = resultResponse.articles
                   oldArticle?.addAll(newArticle)
               }

               return Resource.Success(breakingNewsResponse ?:resultResponse)
           }
       }

        return Resource.Error(response.message())
    }

    private fun handelSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse>{
       if(response.isSuccessful){
           response.body()?.let { resultResponse ->
               searchNewsPage++
               if (searchNewsResponse == null){
                   searchNewsResponse = resultResponse
               }else{
                   val oldArticle = searchNewsResponse?.articles
                   val newArticle = resultResponse.articles
                   oldArticle?.addAll(newArticle)
               }

               return Resource.Success(searchNewsResponse ?:resultResponse)
           }
       }

        return Resource.Error(response.message())
    }



    // -------------------- Handle Response --------------------
//    private fun handleNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
//        return if(response.isSuccessful){
//            response.body()?.let { Resource.Success(it) }
//                ?: Resource.Error("Empty Response")
//        } else {
//            // parse error body JSON if available
//            val errorMsg = try {
//                val gson = Gson()
//                response.errorBody()?.string()?.let {
//                    gson.fromJson(it, ErrorResponse::class.java).message
//                } ?: response.message()
//            } catch (e: Exception) {
//                response.message()
//            }
//            Resource.Error(errorMsg ?: "Unknown error")
//        }
//    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.insert(article)
    }

    fun getSaveArticle() = newsRepository.getAllArticles()

    fun deleteArticle(article: Article)= viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }

}