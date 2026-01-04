package com.emranhss.news.repository

import com.emranhss.news.api.RetrofitInstance
import com.emranhss.news.db.ArticleDatabase

class NewsRepository (
    val db: ArticleDatabase
){

    suspend fun getBreakingNews(countryCode: String,pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode,pageNumber)

    suspend fun searchNews(searchQueary : String,pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQueary,pageNumber)


//    suspend fun getBreakingNewsBySource(source: String, pageNumber: Int) =
//        RetrofitInstance.api.getBreakingNewsBySource(source, pageNumber)
//
//    suspend fun getBreakingNewsByCategory(countryCode: String, category: String, pageNumber: Int) =
//        RetrofitInstance.api.getBreakingNewsByCategory(countryCode, category, pageNumber)
//

}