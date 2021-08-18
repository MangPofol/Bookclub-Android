package com.example.bookclub.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookclub.R
import com.example.bookclub.databinding.BookListPagerBinding
import com.example.bookclub.fragment.SortFilterFragment

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
        val filterLayout: FrameLayout = itemView.filterLayout
        val recyclerView: RecyclerView = itemView.bookListRecyclerView
    }

    fun clearFilterLayout() {
        binding.filterLayout.removeAllViews()
    }

    fun setVisibilityFilterLayout(visibility: Int) {
        binding.filterLayout.visibility = visibility
    }

    fun addFilterLayout(fragmentManager: FragmentManager, radioId: Int) {
        binding.filterLayout.removeAllViews()
        when (radioId) {
            R.id.searchButton -> {

            }
            R.id.clubButton -> {

            }
            R.id.sortButton -> {
                fragmentManager.beginTransaction().replace(binding.filterLayout.id, SortFilterFragment()).commit()
            }
        }
    }
}