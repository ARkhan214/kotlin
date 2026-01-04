package com.emranhss.news.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.emranhss.news.R
import com.emranhss.news.adapters.NewsAdapter
import com.emranhss.news.databinding.FragmentSearchNewsBinding
import com.emranhss.news.ui.NewsActivity
import com.emranhss.news.ui.NewsViewModel
import com.emranhss.news.util.Resource

class SearchNewsFragment : Fragment(R.layout.fragment_search_news) {
    lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter

    private var _binding: FragmentSearchNewsBinding? = null
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel

        viewModel.searchNews.observe(viewLifecycleOwner) { resource ->
            when(resource) {
                is Resource.Success -> {
                    hideProgressBar()
                    resource.data?.let {
                        Log.d("BreakingNews", "Articles size: ${it.articles.size}")
                        newsAdapter.differ.submitList(it.articles)
                    }?:Log.d("BreakingNews", "Response data is null")
                }
                is Resource.Error -> {
                    hideProgressBar()

                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }

    }


    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.rvSearchNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}