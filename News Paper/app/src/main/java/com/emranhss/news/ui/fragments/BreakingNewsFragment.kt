package com.emranhss.news.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.emranhss.news.R
import com.emranhss.news.adapters.NewsAdapter
import com.emranhss.news.databinding.FragmentBreakingNewsBinding
import com.emranhss.news.ui.NewsViewModel
import com.emranhss.news.util.Resource
import androidx.fragment.app.activityViewModels
import com.emranhss.news.db.ArticleDatabase
import com.emranhss.news.repository.NewsRepository
import com.emranhss.news.ui.NewsViewModelProviderFactory

class BreakingNewsFragment : Fragment() {

    private var _binding: FragmentBreakingNewsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NewsViewModel by activityViewModels {
        NewsViewModelProviderFactory(NewsRepository(ArticleDatabase(requireContext())))
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

        viewModel.breakingNews.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    hideProgressBar()
                    resource.data?.let {
                        Log.d("BreakingNews", "Articles size: ${it.articles.size}")
                        newsAdapter.differ.submitList(it.articles)
                    } ?: Log.d("BreakingNews", "Response data is null")
                }

                is Resource.Error -> {
                    hideProgressBar()

//                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()

                    Toast.makeText(
                        requireContext(),
                        resource.message ?: "Something went wrong",
                        Toast.LENGTH_SHORT
                    ).show()

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
        binding.rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}