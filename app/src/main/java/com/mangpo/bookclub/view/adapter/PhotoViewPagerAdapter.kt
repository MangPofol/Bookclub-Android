package com.mangpo.bookclub.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mangpo.bookclub.databinding.ItemPhotoViewBinding
import java.util.ArrayList

class PhotoViewPagerAdapter(private val photos: ArrayList<String>): RecyclerView.Adapter<PhotoViewPagerAdapter.PagerViewHolder>() {

    private lateinit var binding: ItemPhotoViewBinding

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PhotoViewPagerAdapter.PagerViewHolder {
        binding = ItemPhotoViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PagerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewPagerAdapter.PagerViewHolder, position: Int) {
        Glide.with(holder.itemView).load(photos[position]).into(holder.postImg)
    }

    override fun getItemCount(): Int = photos.size

    inner class PagerViewHolder(itemView: ItemPhotoViewBinding): RecyclerView.ViewHolder(itemView.root) {
        val postImg: ImageView = itemView.postIv
    }

}