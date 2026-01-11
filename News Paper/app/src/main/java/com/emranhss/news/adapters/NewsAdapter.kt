package com.emranhss.news.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emranhss.news.databinding.ItemArticlePreviewBinding
import com.emranhss.news.models.Article
import com.emranhss.news.R


//First chanege
//class NewsAdapter(
//    private val onItemClick: (Article) -> Unit
//) : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {


//=================st=====================
class NewsAdapter(
    private val onItemClick: (Article) -> Unit,
    private val onHeartClick: (Article) -> Unit
) : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    //==============end======================
    inner class ArticleViewHolder(
        val binding: ItemArticlePreviewBinding
    ) : RecyclerView.ViewHolder(binding.root)


    //=============st===================
    //second change
    private var savedArticles: List<Article> = emptyList()

    //third change
    fun setSavedArticles(articles: List<Article>) {
        savedArticles = articles
        notifyDataSetChanged()
    }
//===============end====================

    private val differCallback = object : DiffUtil.ItemCallback<Article>() {

        override fun areItemsTheSame(
            oldItem: Article,
            newItem: Article
        ): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(
            oldItem: Article,
            newItem: Article
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ArticleViewHolder {
        val binding = ItemArticlePreviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ArticleViewHolder(binding)
    }

    override fun getItemCount(): Int {

        return differ.currentList.size

    }

//================st=========================
//    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
//
//        val article = differ.currentList[position]
//        holder.binding.apply {
//            Glide.with(holder.itemView.context).load(article.urlToImage).into(ivArticleImage)
//            tvSource.text = article.source?.name?:"Unknown Source"
//            tvTitle.text = article.title?:"No Title"
//            tvDescription.text = article.description?:""
//            tvPublishedAt.text = article.publishedAt?:""
//            holder.itemView.setOnClickListener {
//                onItemClick(article)
//            }
//        }
//    }


    //fourth change
    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]

        holder.binding.apply {
            Glide.with(root.context)
                .load(article.urlToImage)
                .into(ivArticleImage)

            tvSource.text = article.source?.name ?: "Unknown"
            tvTitle.text = article.title ?: ""
            tvDescription.text = article.description ?: ""
            tvPublishedAt.text = article.publishedAt ?: ""

            // Check saved or not
            val isSaved = savedArticles.any { it.url == article.url }

            ivHeart.setImageResource(
                if (isSaved) R.drawable.ic_fab_filled
                else R.drawable.ic_fab_outline
            )

            ivHeart.setOnClickListener {
                onHeartClick(article)
            }

            root.setOnClickListener {
                onItemClick(article)
            }
        }
    }
//=================end===================================

}