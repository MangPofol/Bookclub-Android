package com.mangpo.bookclub.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mangpo.bookclub.databinding.ItemPostBinding
import com.mangpo.bookclub.model.PostDetailModel

class PostAdapter() : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    private lateinit var binding: ItemPostBinding
    private lateinit var myItemClickListener: MyItemClickListener

    private val posts: ArrayList<PostDetailModel> = ArrayList<PostDetailModel>()

    interface MyItemClickListener {
        fun goPostDetail(post: PostDetailModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostAdapter.PostViewHolder {
        binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostAdapter.PostViewHolder, position: Int) {
        holder.title.text = posts[position].title
        holder.date.text = posts[position].createdDate.substring(0, 10)

        holder.title.setOnClickListener {
            myItemClickListener.goPostDetail(posts[position])
        }
    }

    override fun getItemCount(): Int = posts.size

    fun setMyItemClickListener(myItemClickListener: MyItemClickListener) {
        this.myItemClickListener = myItemClickListener
    }

    fun setData(posts: List<PostDetailModel>) {
        this.posts.clear()
        if (posts.isNotEmpty())
            this.posts.addAll(posts)

        notifyDataSetChanged()
    }

    inner class PostViewHolder(itemView: ItemPostBinding) : RecyclerView.ViewHolder(itemView.root) {
        val title: TextView = itemView.postTitleTv
        val date: TextView = itemView.postDateTv
    }
}