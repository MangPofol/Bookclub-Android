package com.example.bookclub.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookclub.databinding.BookItemBinding
import com.example.bookclub.model.NaverBookModel

class BookAdapter() :
    RecyclerView.Adapter<BookAdapter.BookViewHolder>() {
    private lateinit var binding: BookItemBinding
    private lateinit var bookItemClick: OnBookItemClick
    private var books: MutableList<NaverBookModel> = ArrayList<NaverBookModel>()

    constructor(bookItemClick: OnBookItemClick): this() {
        this.bookItemClick = bookItemClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookAdapter.BookViewHolder {
        binding = BookItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookAdapter.BookViewHolder, position: Int) {
        holder.bookImg.clipToOutline = true
        Glide.with(holder.bookImg)
            .load(books[position].image)
            .into(holder.bookImg)

        holder.bookTitle.text = books[position].title

        //책 이미지가 클릭됐을 때 리스너
        holder.bookImg.setOnClickListener {
            bookItemClick.onClick(holder.bookTitle.text.toString())
        }
    }

    override fun getItemCount(): Int {
        return books.size
    }

    inner class BookViewHolder(itemView: BookItemBinding) : RecyclerView.ViewHolder(itemView.root) {
        val bookImg: ImageView = itemView.bookIV
        val bookTitle: TextView = itemView.bookTitle

    }

    fun setBooks(bookList: MutableList<NaverBookModel>) {
        books = bookList
        notifyDataSetChanged()
    }
}