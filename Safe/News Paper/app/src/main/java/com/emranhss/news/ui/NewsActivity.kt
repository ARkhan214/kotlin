package com.emranhss.news.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.emranhss.news.R
import com.emranhss.news.databinding.ActivityNewsBinding
import com.emranhss.news.db.ArticleDatabase
import com.emranhss.news.repository.NewsRepository


class NewsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewsBinding
    lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)

        // NavController
        val navController = supportFragmentManager
            .findFragmentById(R.id.newsNavHostFragment)
            ?.findNavController()


        // BottomNavigationView setup
        navController?.let {
            binding.bottomNavigationView.setupWithNavController(it)
        }
    }
}