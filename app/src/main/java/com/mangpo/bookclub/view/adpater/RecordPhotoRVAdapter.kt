package com.mangpo.bookclub.view.adpater

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mangpo.bookclub.databinding.ItemRecordPhotoBinding

class RecordPhotoRVAdapter(): RecyclerView.Adapter<RecordPhotoRVAdapter.RecordPhotoViewHolder>() {
    interface MyClickListener {
        fun removePhoto(photos: ArrayList<String>)
    }

    private var photos: ArrayList<String> = arrayListOf()

    private lateinit var binding: ItemRecordPhotoBinding
    private lateinit var myClickListener: MyClickListener

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecordPhotoRVAdapter.RecordPhotoViewHolder {
        binding = ItemRecordPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RecordPhotoViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: RecordPhotoRVAdapter.RecordPhotoViewHolder,
        position: Int
    ) {
        holder.image.clipToOutline = true
        if (photos[position].startsWith("https"))
            Glide.with(binding.root).load(photos[position]).into(holder.image)
        else
            Glide.with(binding.root).load(Uri.parse(photos[position])).into(holder.image)

        holder.removeIv.setOnClickListener {
            photos.removeAt(position)
            notifyDataSetChanged()
            myClickListener.removePhoto(photos)
        }
    }

    override fun getItemCount(): Int = photos.size

    fun setData(photos: ArrayList<String>) {
        this.photos = photos
    }

    fun setMyClickListener(myClickListener: MyClickListener) {
        this.myClickListener = myClickListener
    }

    inner class RecordPhotoViewHolder(itemView: ItemRecordPhotoBinding): RecyclerView.ViewHolder(itemView.root) {
        val image: ImageView = itemView.recordPhotoIv
        val removeIv: ImageView = itemView.recordPhotoRemoveIv
    }
}