package com.mangpo.ourpage.view.adpater

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mangpo.ourpage.databinding.ItemPhotoViewBinding

class PhotoViewVPAdapter(): RecyclerView.Adapter<PhotoViewVPAdapter.PhotoViewViewHolder>() {
    private var photos: List<String> = listOf()

    private lateinit var binding: ItemPhotoViewBinding

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PhotoViewVPAdapter.PhotoViewViewHolder {
        binding = ItemPhotoViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PhotoViewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewVPAdapter.PhotoViewViewHolder, position: Int) {
        Glide.with(binding.root.context).load(photos[position]).into((holder.image))
    }

    override fun getItemCount(): Int = photos.size

    inner class PhotoViewViewHolder(itemView: ItemPhotoViewBinding): RecyclerView.ViewHolder(itemView.root) {
        val image: ImageView = itemView.photoIv
    }

    fun setData(photos: List<String>) {
        this.photos = photos
        notifyDataSetChanged()
    }
}