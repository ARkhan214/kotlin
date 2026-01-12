package com.emranhss.news.repository

import com.emranhss.news.api.RetrofitInstance
import com.emranhss.news.db.ArticleDatabase
import com.emranhss.news.models.Article

class NewsRepository(
    val db: ArticleDatabase
) {

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

    suspend fun searchNews(searchQueary: String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQueary, pageNumber)


    suspend fun insert(article: Article) = db.getArticleDao().insert(article)
    fun getAllArticles() = db.getArticleDao().getAllArticles()
    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)

}