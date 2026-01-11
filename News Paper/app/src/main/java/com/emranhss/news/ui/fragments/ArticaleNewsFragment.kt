package com.emranhss.news.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.emranhss.news.databinding.FragmentArticaleNewsBinding
import com.emranhss.news.ui.NewsActivity
import com.emranhss.news.ui.NewsViewModel
import com.google.android.material.snackbar.Snackbar

class ArticaleNewsFragment : Fragment() {
    lateinit var viewModel: NewsViewModel
    private val args: ArticaleNewsFragmentArgs by navArgs()

    private var _binding: FragmentArticaleNewsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticaleNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    //-----------
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel

        val article = args.article
        article?.url?.let { url ->
            binding.webView.apply {
                webViewClient = WebViewClient()
                settings.javaScriptEnabled = true
                loadUrl(url)
                Log.d("WEBVIEW_URL", url)
            }
        }

        binding.fab.setOnClickListener {
            article?.let {
                viewModel.saveArticle(it) { message ->
                    Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
                }
            }
        }

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}