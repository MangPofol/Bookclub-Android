package com.mangpo.bookclub.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mangpo.bookclub.databinding.ItemRecordImgBinding

class RecordImgRVAdapter() :
    RecyclerView.Adapter<RecordImgRVAdapter.RecordImgHolder>() {
    private lateinit var binding: ItemRecordImgBinding
    private lateinit var mItemClick: MyItemClickListener

    private val pictures: ArrayList<String> = arrayListOf()

    interface MyItemClickListener {
        fun removeItem(path: String)
    }

    fun setMyItemClickListener(mItemClick: MyItemClickListener) {
        this.mItemClick = mItemClick
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecordImgRVAdapter.RecordImgHolder {
        binding =
            ItemRecordImgBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RecordImgHolder(binding)
    }

    override fun onBindViewHolder(holder: RecordImgRVAdapter.RecordImgHolder, position: Int) {
        holder.iv.clipToOutline = true
        Glide.with(binding.root.context.applicationContext).load(pictures[position]).into(holder.iv)

        //삭제 아이콘 누르면 이미지 삭제하기
        holder.removeBtn.setOnClickListener {
            mItemClick.removeItem(pictures[position])
        }
    }

    override fun getItemCount(): Int {
        return pictures.size
    }

    fun setData(imgList: List<String>) {
        pictures.clear()
        pictures.addAll(imgList)
        notifyDataSetChanged()
    }

    class RecordImgHolder(itemView: ItemRecordImgBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val iv: ImageView = itemView.recordIv
        val removeBtn: View = itemView.removeImgView
    }
}