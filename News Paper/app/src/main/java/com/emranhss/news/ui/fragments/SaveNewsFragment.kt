package com.emranhss.news.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.emranhss.news.R
import com.emranhss.news.ui.NewsActivity
import com.emranhss.news.ui.NewsViewModel

class SaveNewsFragment : Fragment(R.layout.fragment_save_news) {

    lateinit var viewModel: NewsViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel

    }

}