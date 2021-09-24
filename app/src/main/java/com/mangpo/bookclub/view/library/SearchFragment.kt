package com.mangpo.bookclub.view.library

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.mangpo.bookclub.databinding.FragmentSearchBinding
import com.mangpo.bookclub.model.BookModel
import com.mangpo.bookclub.view.adapter.BookAdapter
import com.mangpo.bookclub.viewmodel.BookViewModel
import com.mangpo.bookclub.viewmodel.MyLibraryViewModel

class SearchFragment(adapter: BookAdapter) : Fragment(), TextWatcher {
    private lateinit var binding: FragmentSearchBinding

    private var books: MutableList<BookModel> = ArrayList<BookModel>()

    private val bookAdapter: BookAdapter = adapter
    private val myLibraryViewModel: MyLibraryViewModel by activityViewModels<MyLibraryViewModel>()
    private val bookViewModel: BookViewModel by activityViewModels<BookViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        binding.searchBookET.addTextChangedListener(this)

        when(myLibraryViewModel.libraryReadType.value) {
            0 -> books = bookViewModel.nowBooks.value!!
            1 -> books = bookViewModel.afterBooks.value!!
            2 -> books = bookViewModel.beforeBooks.value!!
        }

        bookAdapter.setBooks(books)

        return binding.root
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        bookAdapter.setBooks(books.filter {
            it.name!!.contains(s!!)
        } as MutableList<BookModel>)
    }

    override fun afterTextChanged(s: Editable?) {

    }


}