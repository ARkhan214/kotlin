package com.emranhss.news.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emranhss.news.api.RetrofitInstance
import com.emranhss.news.models.NewsResponse
import com.emranhss.news.repository.NewsRepository
import com.emranhss.news.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(val newsRepository: NewsRepository): ViewModel() {

    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1

    init {
        getBreakingNews(contryCode = "us")
    }

//    fun getBreakingNews(contryCode: String) = viewModelScope.launch {
//      breakingNews.postValue(Resource.Loading())
//      val response = newsRepository.getBreakingNews(contryCode,breakingNewsPage)
//        breakingNews.postValue(handelBreakingNewsResponse(response))
//    }

    fun getBreakingNews(contryCode: String) = viewModelScope.launch {
        breakingNews.postValue(Resource.Loading())
        try {
            val response = newsRepository.getBreakingNews(contryCode, breakingNewsPage)
            breakingNews.postValue(handelBreakingNewsResponse(response))
        } catch (e: Exception) {
            breakingNews.postValue(Resource.Error("$e"))
        }
    }


//    fun searchNews(searchQueary : String) = viewModelScope.launch{
//        searchNews.postValue(Resource.Loading())
//        val response  = newsRepository.searchNews(searchQueary,searchNewsPage)
//        searchNews.postValue(handelSearchNewsResponse(response))
//    }

    fun searchNews(searchQueary: String) = viewModelScope.launch {
        searchNews.postValue(Resource.Loading())
        try {
            val response = newsRepository.searchNews(searchQueary, searchNewsPage)
            searchNews.postValue(handelSearchNewsResponse(response))
        } catch (e: Exception) {
            searchNews.postValue(Resource.Error("$e"))
        }
    }

    private fun handelBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse>{
       if(response.isSuccessful){
           response.body()?.let { resultResponse ->
               return Resource.Success(resultResponse)
           }
       }

        return Resource.Error(response.message())
    }

    private fun handelSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse>{
       if(response.isSuccessful){
           response.body()?.let { resultResponse ->
               return Resource.Success(resultResponse)
           }
       }

        return Resource.Error(response.message())
    }


}