package com.emranhss.news.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emranhss.news.R
import com.emranhss.news.adapters.NewsAdapter
import com.emranhss.news.databinding.FragmentSearchNewsBinding
import com.emranhss.news.ui.NewsActivity
import com.emranhss.news.ui.NewsViewModel
import com.emranhss.news.util.Constants
import com.emranhss.news.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment : Fragment() {
    lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter

    private var _binding: FragmentSearchNewsBinding? = null
    private val binding get() = _binding!!
    private var searchJob: Job? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchNewsBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        setupRecyclerView()

//way 1------------
        binding.etSearch.addTextChangedListener { text ->
            val query = text?.toString()?.trim().orEmpty()
            if (query.isEmpty()) return@addTextChangedListener

            searchJob?.cancel()
            searchJob = viewLifecycleOwner.lifecycleScope.launch {
                delay(Constants.SEARCH_NEWS_TIME_DELAY)
                viewModel.searchNews(query)
            }
        }

        viewModel.searchNews.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    hideProgressBar()
                    resource.data?.let {newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())

                        val  totakPages = newsResponse.totalResults / Constants.QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.searchNewsPage == totakPages
                        if (isLastPage){
                            binding.rvSearchNews.setPadding(0,0,0,0)
                        }
                    } ?: Log.d("Search News ", "Response data is null")
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
        isLoading = false
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }



    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollListener = object : RecyclerView.OnScrollListener(){

        override fun onScrolled(
            recyclerView: RecyclerView,
            dx: Int,
            dy: Int
        ) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Constants.Companion.QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate){
                viewModel.searchNews(binding.etSearch.text.toString())
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(
            recyclerView: RecyclerView,
            newState: Int
        ) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }

    }


    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter(onItemClick = {
            val bundel = Bundle().apply {
                putParcelable("article",it)
            }
            findNavController().navigate(
                R.id.action_searchNewsFragment_to_articaleNewsFragment,bundel
            )
        })
        binding.rvSearchNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addOnScrollListener(this@SearchNewsFragment.scrollListener)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}