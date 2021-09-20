package com.example.bookclub.view.library

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.bookclub.databinding.FragmentSearchBinding
import com.example.bookclub.model.BookModel
import com.example.bookclub.view.adapter.BookAdapter
import com.example.bookclub.viewmodel.BookViewModel

class SearchFragment(adapter: BookAdapter) : Fragment(), TextWatcher {
    private lateinit var binding: FragmentSearchBinding
    private val bookViewModel: BookViewModel by activityViewModels()
    private val bookAdapter: BookAdapter = adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        binding.searchBookET.addTextChangedListener(this)

        return binding.root
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (count!=0) {
            when (bookViewModel.readType.value) {
                0 -> bookAdapter.setBooks(bookViewModel.nowBooks.value!!.filter {
                    it.name.contains(s!!)
                } as MutableList<BookModel>)

                1 -> bookAdapter.setBooks(bookViewModel.afterBooks.value!!.filter {
                    it.name.contains(s!!)
                } as MutableList<BookModel>)

                2 -> bookAdapter.setBooks(bookViewModel.beforeBooks.value!!.filter {
                    it.name.contains(s!!)
                } as MutableList<BookModel>)
            }
        } else {
            when (bookViewModel.readType.value) {
                0 -> bookAdapter.setBooks(bookViewModel.nowBooks.value!!)
                1 -> bookAdapter.setBooks(bookViewModel.afterBooks.value!!)
                2 -> bookAdapter.setBooks(bookViewModel.beforeBooks.value!!)
            }
        }
    }

    override fun afterTextChanged(s: Editable?) {

    }


}