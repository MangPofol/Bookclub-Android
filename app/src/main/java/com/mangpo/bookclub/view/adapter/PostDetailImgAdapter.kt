package com.mangpo.bookclub.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mangpo.bookclub.databinding.ItemPostDetailImgBinding

class PostDetailImgAdapter() :
    RecyclerView.Adapter<PostDetailImgAdapter.PostDetailImgViewHolder>() {
    private lateinit var binding: ItemPostDetailImgBinding
    private lateinit var myItemClickListener: MyItemClickListener

    private val imgList: ArrayList<String> = arrayListOf()

    interface MyItemClickListener {
        fun goPhotoView(position: Int)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostDetailImgAdapter.PostDetailImgViewHolder {
        binding =
            ItemPostDetailImgBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostDetailImgViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PostDetailImgAdapter.PostDetailImgViewHolder,
        position: Int
    ) {
        Glide.with(binding.root.context.applicationContext).load(imgList[position])
            .into(holder.image)
        holder.image.setOnClickListener {
            myItemClickListener.goPhotoView(position)
        }
    }

    override fun getItemCount(): Int = imgList.size

    fun setData(imgList: List<String>) {
        this.imgList.clear()
        this.imgList.addAll(imgList)
        notifyDataSetChanged()
    }

    fun setMyItemClickListener(myItemClickListener: PostDetailImgAdapter.MyItemClickListener) {
        this.myItemClickListener = myItemClickListener
    }

    inner class PostDetailImgViewHolder(itemView: ItemPostDetailImgBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val image: ImageView = itemView.postIv
    }
}