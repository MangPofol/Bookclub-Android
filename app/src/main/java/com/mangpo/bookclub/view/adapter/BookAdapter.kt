package com.mangpo.bookclub.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mangpo.bookclub.databinding.BookItemBinding
import com.mangpo.bookclub.model.BookModel
import com.mangpo.bookclub.model.KakaoBookModel

class BookAdapter() :
    RecyclerView.Adapter<BookAdapter.BookViewHolder>() {
    private lateinit var binding: BookItemBinding
    private lateinit var itemClick: OnItemClick

    private var books: MutableList<Any> = ArrayList<Any>()
    private var type: String = "library"

    constructor(itemClick: OnItemClick) : this() {
        this.itemClick = itemClick
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

        //책 이미지가 클릭됐을 때 리스너
        holder.bookImg.setOnClickListener {
            itemClick.onClick(position)
        }
    }

    private fun libraryViewBinding(position: Int, holder: BookViewHolder) {
        val book: BookModel = books[position] as BookModel

        Glide.with(binding.root.context.applicationContext)
            .load(book.imgPath)
            .into(holder.bookImg)

        holder.bookTitle.text = book.name
    }

    private fun writeViewBinding(position: Int, holder: BookViewHolder) {
        val book: KakaoBookModel = books[position] as KakaoBookModel

        Glide.with(binding.root.context.applicationContext)
            .load(book.thumbnail)
            .into(holder.bookImg)

        holder.bookTitle.text = book.title
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

    fun setBooks(bookList: MutableList<BookModel>?) {
        if (bookList != null) {
            books = bookList.toMutableList()
        } else {
            books.removeAll(books)
        }

        notifyDataSetChanged()
        type = "library"
    }
}