package com.emranhss.news.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.emranhss.news.adapters.NewsAdapter
import com.emranhss.news.databinding.FragmentBreakingNewsBinding
import com.emranhss.news.ui.NewsViewModel
import com.emranhss.news.util.Resource
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.emranhss.news.db.ArticleDatabase
import com.emranhss.news.repository.NewsRepository
import com.emranhss.news.ui.NewsViewModelProviderFactory
import com.emranhss.news.util.Constants

class BreakingNewsFragment : Fragment() {

    private var _binding: FragmentBreakingNewsBinding? = null
    private val binding get() = _binding!!


    private val viewModel: NewsViewModel by activityViewModels {
        NewsViewModelProviderFactory(
            requireActivity().application, NewsRepository(
                ArticleDatabase(requireContext())


//            NewsRepository(
//                ArticleDatabase(requireContext())
            )
        )
    }

    private lateinit var newsAdapter: NewsAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBreakingNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.breakingNewsPage = 1
            viewModel.getBreakingNews("us")

            // Optional auto-stop after 3 sec
            binding.swipeRefresh.postDelayed({
                if (binding.swipeRefresh.isRefreshing) {
                    binding.swipeRefresh.isRefreshing = false
                }
            }, 3000)
        }

        viewModel.breakingNews.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    hideProgressBar()
                    resource.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())

                        val totalPages =
                            (newsResponse.totalResults + Constants.QUERY_PAGE_SIZE - 1) / Constants.QUERY_PAGE_SIZE

                        val reachedLastPage = viewModel.breakingNewsPage > totalPages

                        if (reachedLastPage && !isLastPage) {
                            Toast.makeText(
                                requireContext(), "Loading More News...", Toast.LENGTH_SHORT
                            ).show()
                            binding.rvBreakingNews.setPadding(0, 0, 0, 0)

                        }
                        isLastPage = reachedLastPage
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    Toast.makeText(
                        requireContext(),
                        resource.message ?: "Unknown error",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is Resource.Loading -> {
                    showProgressBar()

                }
            }
        }
        //===========st===========
        viewModel.getSaveArticle().observe(viewLifecycleOwner) { savedList ->
            newsAdapter.setSavedArticles(savedList)
        }
        //============end====
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

    val scrollListener = object : RecyclerView.OnScrollListener() {

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
            val shouldPaginate =
                isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate) {
                viewModel.getBreakingNews("us")
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(
            recyclerView: RecyclerView,
            newState: Int
        ) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

    }


    private fun setupRecyclerView() {
        //===st===
        newsAdapter = NewsAdapter(
            onItemClick = { article ->
                val action = BreakingNewsFragmentDirections
                    .actionBreakingNewsFragmentToArticaleNewsFragment(article)
                findNavController().navigate(action)
            },
            onHeartClick = { article ->
                viewModel.saveArticle(article) { }
            }
        )
//==end====

//        newsAdapter = NewsAdapter(onItemClick = { article ->
//            val action = BreakingNewsFragmentDirections
//                .actionBreakingNewsFragmentToArticaleNewsFragment(article) // Safe Args
//            findNavController().navigate(action)
//        })


        binding.rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addOnScrollListener(this@BreakingNewsFragment.scrollListener)

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}