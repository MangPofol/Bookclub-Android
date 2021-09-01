package com.example.bookclub.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookclub.databinding.BookItemBinding
import com.example.bookclub.model.BookModel

class BookAdapter() : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {
    private lateinit var binding: BookItemBinding
    private var books: List<BookModel> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookAdapter.BookViewHolder {
        binding = BookItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookAdapter.BookViewHolder, position: Int) {
        holder.bookImg.clipToOutline = true
    }

    override fun getItemCount(): Int {
        return 100
    }

    inner class BookViewHolder(itemView: BookItemBinding) : RecyclerView.ViewHolder(itemView.root) {
        val bookImg: ImageView = itemView.bookIV
        val bookTitle: TextView = itemView.bookTitle
    }
}