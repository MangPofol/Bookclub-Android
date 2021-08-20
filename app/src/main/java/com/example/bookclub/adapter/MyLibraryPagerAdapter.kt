package com.example.bookclub.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookclub.R
import com.example.bookclub.databinding.BookListPagerBinding
import com.example.bookclub.fragment.BookClubFilterFragment
import com.example.bookclub.fragment.SortFilterFragment
import com.example.bookclub.util.HorizontalItemDecorator
import com.example.bookclub.util.VerticalItemDecorator

class MyLibraryPagerAdapter() : RecyclerView.Adapter<MyLibraryPagerAdapter.MyLibraryPagerViewHolder>() {
    private lateinit var binding: BookListPagerBinding
    private lateinit var context: Context

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyLibraryPagerAdapter.MyLibraryPagerViewHolder {
        binding = BookListPagerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context

        return MyLibraryPagerViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MyLibraryPagerAdapter.MyLibraryPagerViewHolder,
        position: Int
    ) {
        holder.recyclerView.adapter = BookAdapter()
        holder.recyclerView.layoutManager = GridLayoutManager(this.context, 3)
        holder.recyclerView.addItemDecoration(VerticalItemDecorator(60))
        holder.recyclerView.addItemDecoration(HorizontalItemDecorator(20))
    }

    override fun getItemCount(): Int {
        return 3
    }

    class MyLibraryPagerViewHolder(itemView: BookListPagerBinding) : RecyclerView.ViewHolder(itemView.root) {
        val recyclerView: RecyclerView = itemView.bookListRecyclerView
    }
}