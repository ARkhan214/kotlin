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
        NewsViewModelProviderFactory(NewsRepository(ArticleDatabase(requireContext())))
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
//        viewModel = (activity as NewsActivity).viewModel
        setupRecyclerView()
//        newsAdapter.setOnItemClickListener {
//            val bundel = Bundle().apply {
//                putParcelable("article",it)
//            }
//            findNavController().navigate(
//                R.id.action_saveNewsFragment_to_articaleNewsFragment,bundel
//            )
//        }

        val itemTochHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
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

                Snackbar.make(view,"News Deleted Succesfull", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo"){
                        viewModel.saveArticle(article)
                    }
                        show()
                }
            }
        }

        ItemTouchHelper(itemTochHelperCallback).apply {
            attachToRecyclerView(binding.rvSavedNews)
        }

        viewModel.getSaveArticle().observe(viewLifecycleOwner, Observer{articles ->
            newsAdapter.differ.submitList(articles)
        })

    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter(onItemClick = { article ->
            val action = SaveNewsFragmentDirections
                .actionSaveNewsFragmentToArticaleNewsFragment(article)
            findNavController().navigate(action)
        })
        binding.rvSavedNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

}