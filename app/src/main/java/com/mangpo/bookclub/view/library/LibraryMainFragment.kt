package com.mangpo.bookclub.view.library

import android.os.Bundle
import android.util.Log
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

        if (childFragmentManager.fragments.size==0)
            childFragmentManager.beginTransaction()
                    .add(binding.libraryMainFrameLayout.id, MyLibraryFragment())
                    .addToBackStack("myLibrary").commitAllowingStateLoss()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        Log.d("LibraryMainFragment", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("LibraryMainFragment", "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("LibraryMainFragment", "onDestroy")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("LibraryMainFragment", "onDestroyView")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("LibraryMainFragment", "onDetach")
    }

    fun moveToBookDesc(book: BookModel) {
        childFragmentManager.beginTransaction()
                .replace(binding.libraryMainFrameLayout.id, BookDescFragment().apply {
                    arguments = Bundle().apply {
                        putString("book", Gson().toJson(book))
                    }
                }).addToBackStack("bookDesc").commitAllowingStateLoss()
    }

}