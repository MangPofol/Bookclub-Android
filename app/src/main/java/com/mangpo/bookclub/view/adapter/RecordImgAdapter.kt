package com.mangpo.bookclub.view.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mangpo.bookclub.databinding.RecordSelectImgItemBinding

class RecordImgAdapter(itemClick: OnItemClick) :
    RecyclerView.Adapter<RecordImgAdapter.RecordImgHolder>() {
    private lateinit var binding: RecordSelectImgItemBinding

    private val itemClick: OnItemClick = itemClick
    private var uriList: MutableList<Uri> = ArrayList<Uri>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecordImgAdapter.RecordImgHolder {
        binding =
            RecordSelectImgItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RecordImgHolder(binding)
    }

    override fun onBindViewHolder(holder: RecordImgAdapter.RecordImgHolder, position: Int) {
        holder.iv.clipToOutline = true
        Glide.with(holder.itemView).load(uriList[position]).into(holder.iv)

        holder.removeBtn.setOnClickListener {
            uriList.removeAt(position)
            itemClick.onClick(uriList.size)
            notifyItemRemoved(position)
        }
    }

    override fun getItemCount(): Int {
        return uriList.size
    }

    fun setData(uriList: List<Uri>?) {
        if (uriList == null) {
            this.uriList.clear()
        } else {
            this.uriList = uriList as MutableList<Uri>
        }

        notifyDataSetChanged()
    }

    class RecordImgHolder(itemView: RecordSelectImgItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val iv: ImageView = itemView.recordIv
        val removeBtn: View = itemView.removeImgView
    }
}