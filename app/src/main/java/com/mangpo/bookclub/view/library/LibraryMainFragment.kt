package com.mangpo.bookclub.view.library

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.mangpo.bookclub.databinding.FragmentLibraryMainBinding
import com.mangpo.bookclub.model.BookModel

class LibraryMainFragment : Fragment() {

    private lateinit var binding: FragmentLibraryMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLibraryMainBinding.inflate(inflater, container, false)

        childFragmentManager.beginTransaction()
            .replace(binding.libraryMainFrameLayout.id, MyLibraryFragment())
            .addToBackStack("myLibrary").commit()

        return binding.root
    }

    fun moveToBookDesc(book: BookModel) {
        childFragmentManager.beginTransaction()
            .replace(binding.libraryMainFrameLayout.id, BookDescFragment().apply {
                arguments = Bundle().apply {
                    putString("book", Gson().toJson(book))
                }
            }).addToBackStack("bookDesc").commit()
    }

}