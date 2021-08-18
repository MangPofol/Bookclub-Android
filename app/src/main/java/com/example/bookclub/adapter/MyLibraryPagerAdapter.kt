package com.example.bookclub.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookclub.databinding.BookListPagerBinding

class MyLibraryPagerAdapter() : RecyclerView.Adapter<MyLibraryPagerAdapter.MyLibraryPagerViewHolder>() {
    private lateinit var binding: BookListPagerBinding

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyLibraryPagerAdapter.MyLibraryPagerViewHolder {
        binding = BookListPagerBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyLibraryPagerViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MyLibraryPagerAdapter.MyLibraryPagerViewHolder,
        position: Int
    ) {
        holder.recyclerView.adapter = BookAdapter()
        holder.recyclerView.layoutManager = LinearLayoutManager(holder.recyclerView.context, LinearLayoutManager.VERTICAL, false)
    }

    override fun getItemCount(): Int {
        return 3
    }

    class MyLibraryPagerViewHolder(itemView: BookListPagerBinding) : RecyclerView.ViewHolder(itemView.root) {
        val filterLayout: LinearLayout = itemView.filterLayout
        val recyclerView: RecyclerView = itemView.bookListRecyclerView
    }
}