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
    //------------------
    suspend fun upsertArticle(article: Article): String {
        // First, check if the article is already saved
    val isSaved = db.getArticleDao().isArticleSaved(article.url)

    return if (isSaved) {
        // If already saved, return this message
        "Article already saved!"
    } else {
        // If not saved, insert the article
        db.getArticleDao().insert(article)
        "Article saved successfully!"
    }
}

//-------------------
//    suspend fun insert(article: Article) = db.getArticleDao().insert(article)
//
//    suspend fun upsertArticle(article: Article): String {
//        val isSaved = db.getArticleDao().isArticleSaved(article.url)
//        return if (isSaved) {
//            "Article already saved!"
//        } else {
//            db.getArticleDao().insert(article)
//            "Article saved successfully!"
//        }
//    }
// -----------------

    fun getAllArticles() = db.getArticleDao().getAllArticles()
    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)

}