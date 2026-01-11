package com.emranhss.news.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emranhss.news.R
import com.emranhss.news.adapters.NewsAdapter
import com.emranhss.news.databinding.FragmentBreakingNewsBinding
import com.emranhss.news.databinding.FragmentSaveNewsBinding
import com.emranhss.news.databinding.FragmentSearchNewsBinding
import com.emranhss.news.db.ArticleDatabase
import com.emranhss.news.db.ArticleDatabase.Companion.invoke
import com.emranhss.news.repository.NewsRepository
import com.emranhss.news.ui.NewsActivity
import com.emranhss.news.ui.NewsViewModel
import com.emranhss.news.ui.NewsViewModelProviderFactory
import com.google.android.material.snackbar.Snackbar
import kotlin.getValue

class SaveNewsFragment : Fragment(R.layout.fragment_save_news) {

    //    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    private var _binding: FragmentSaveNewsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NewsViewModel by activityViewModels {
        NewsViewModelProviderFactory(
            requireActivity().application,
            NewsRepository(ArticleDatabase(requireContext()))
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSaveNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()


        viewModel.getSaveArticle().observe(viewLifecycleOwner, Observer { articles ->
            newsAdapter.differ.submitList(articles)
            // ... empty state logic
        })

        // ===== SwipeRefreshLayout =====
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getSaveArticle().observe(viewLifecycleOwner) { savedList ->
                newsAdapter.differ.submitList(savedList)
                binding.swipeRefresh.isRefreshing = false
            }
        }

        val itemTochHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(
                viewHolder: RecyclerView.ViewHolder,
                direction: Int
            ) {
                val position = viewHolder.adapterPosition
                val article = newsAdapter.differ.currentList[position]
                viewModel.deleteArticle(article)

                Snackbar.make(view, "News Deleted Successfully", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {
                        viewModel.saveArticle(article) { message ->
                            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
                        }
                    }
                    show()

                }
            }
        }

        ItemTouchHelper(itemTochHelperCallback).apply {
            attachToRecyclerView(binding.rvSavedNews)
        }

        viewModel.getSaveArticle().observe(viewLifecycleOwner, Observer { articles ->
            newsAdapter.differ.submitList(articles)

            // -------- EMPTY STATE LOGIC --------
            //Database  saved news empty paile tokhon
            // xml file er tvInitialMessage ke visible kortese and xml file er rvSavedNews erake invisible kortese
            if (articles.isEmpty()) {
                binding.tvInitialMessage.visibility = View.VISIBLE
                binding.rvSavedNews.visibility = View.GONE
            } else {
                //Database saved news empty na paile tokhon
                // xml file er rvSavedNews  ke visible kortese and xml file er tvInitialMessage erake invisible kortese
                binding.tvInitialMessage.visibility = View.GONE
                binding.rvSavedNews.visibility = View.VISIBLE
            }
        })

    }

    //===============

//    private fun setupRecyclerView() {
//        newsAdapter = NewsAdapter(onItemClick = { article ->
//            val action = SaveNewsFragmentDirections
//                .actionSaveNewsFragmentToArticaleNewsFragment(article)
//            findNavController().navigate(action)
//        })
//        binding.rvSavedNews.apply {
//            adapter = newsAdapter
//            layoutManager = LinearLayoutManager(requireContext())
//        }
//    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter(
            onItemClick = { article ->
                val action = SaveNewsFragmentDirections
                    .actionSaveNewsFragmentToArticaleNewsFragment(article)
                findNavController().navigate(action)
            },
            onHeartClick = { article ->
                // click = UNSAVE
                viewModel.deleteArticle(article)
            }
        )

        binding.rvSavedNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
//===================

}