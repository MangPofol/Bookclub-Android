package com.mangpo.ourpage.view.adpater

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mangpo.ourpage.R
import com.mangpo.ourpage.databinding.ItemBookBinding
import com.mangpo.ourpage.model.remote.Book
import com.mangpo.ourpage.utils.convertDpToPx
import com.mangpo.ourpage.utils.getDeviceWidth

class BookRVAdapter() : RecyclerView.Adapter<BookRVAdapter.BookViewHolder>() {
    interface MyClickListener {
        fun sendBook(book: Book)
    }

    private lateinit var binding: ItemBookBinding
    private lateinit var myClickListener: MyClickListener

    private var books: List<Book> = ArrayList<Book>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookRVAdapter.BookViewHolder {
        binding = ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookRVAdapter.BookViewHolder, position: Int) {
        val width = (getDeviceWidth() - convertDpToPx(binding.root.context, 60)) / 3
        val height = (13 * width) / 9
        val params = holder.bookImg.layoutParams
        params.width = width
        params.height = height
        holder.bookImg.layoutParams = params
        holder.bookImg.clipToOutline = true
        if (books[position].image.isBlank())
            holder.bookImg.setImageDrawable(ContextCompat.getDrawable(binding.root.context, R.drawable.default_book))
        else
            Glide.with(binding.root.context).load(books[position].image).into(holder.bookImg)
        holder.bookImg.setOnClickListener {
            myClickListener.sendBook(books[position])
        }

        holder.bookTitle.text = books[position].name
        holder.bookTitle.setOnClickListener {
            myClickListener.sendBook(books[position])
        }
    }

    override fun getItemCount(): Int {
        return books.size
    }

    fun setMyClickListener(myClickListener: MyClickListener) {
        this.myClickListener = myClickListener
    }

    fun setData(books: List<Book>) {
        this.books = books
        notifyDataSetChanged()
    }

    fun clearData() {
        this.books = arrayListOf()
        notifyDataSetChanged()
    }

    inner class BookViewHolder(itemView: ItemBookBinding) : RecyclerView.ViewHolder(itemView.root) {
        val bookImg: ImageView = itemView.bookIv
        val bookTitle: TextView = itemView.bookTv

    }
}