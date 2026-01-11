package com.emranhss.news.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.emranhss.news.repository.NewsRepository

class NewsViewModelProviderFactory(
   val app: Application,
    val newsRepository: NewsRepository
) : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(app,newsRepository)as T
    }
}