package com.example.bookclub.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookclub.databinding.BookItemBinding
import com.example.bookclub.model.BookModel
import com.example.bookclub.model.KakaoBookModel

class BookAdapter() :
    RecyclerView.Adapter<BookAdapter.BookViewHolder>() {
    private lateinit var binding: BookItemBinding
    private lateinit var bookItemClick: OnBookItemClick
    private var books: MutableList<Any> = ArrayList<Any>()
    private var type: String = "library"

    constructor(bookItemClick: OnBookItemClick) : this() {
        this.bookItemClick = bookItemClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookAdapter.BookViewHolder {
        binding = BookItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookAdapter.BookViewHolder, position: Int) {
        holder.bookImg.clipToOutline = true

        if (type == "write") {
            writeViewBinding(position, holder)
        } else {
            libraryViewBinding(position, holder)
        }
    }

    private fun libraryViewBinding(position: Int, holder: BookViewHolder) {
        val book: BookModel = books[position] as BookModel

        Glide.with(holder.bookImg)
            .load(book.image)
            .into(holder.bookImg)

        holder.bookTitle.text = book.name
    }

    private fun writeViewBinding(position: Int, holder: BookViewHolder) {
        val book: KakaoBookModel = books[position] as KakaoBookModel

        Glide.with(holder.bookImg)
            .load(book.thumbnail)
            .into(holder.bookImg)

        holder.bookTitle.text = book.title

        //책 이미지가 클릭됐을 때 리스너
        holder.bookImg.setOnClickListener {
            bookItemClick.onClick(position)
        }
    }

    override fun getItemCount(): Int {
        return books.size
    }

    inner class BookViewHolder(itemView: BookItemBinding) : RecyclerView.ViewHolder(itemView.root) {
        val bookImg: ImageView = itemView.bookIV
        val bookTitle: TextView = itemView.bookTitle

    }

    fun setKakaoBooks(bookList: MutableList<KakaoBookModel>?) {
        if (bookList == null) {
            books.clear()
        } else {
            books = bookList.toMutableList()
        }
        notifyDataSetChanged()
        type = "write"
    }

    fun setBooks(bookList: MutableList<BookModel>) {
        books = bookList.toMutableList()
        notifyDataSetChanged()
        type = "library"
    }
}