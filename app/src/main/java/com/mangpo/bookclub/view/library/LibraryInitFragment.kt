package com.mangpo.bookclub.view.library

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mangpo.bookclub.databinding.FragmentLibraryInitBinding

class LibraryInitFragment : Fragment() {

    private lateinit var binding: FragmentLibraryInitBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LibraryMainFragment", "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("LibraryMainFragment", "onCreateView")
        binding = FragmentLibraryInitBinding.inflate(inflater, container, false)

        childFragmentManager.beginTransaction()
            .replace(binding.libraryMainFrameLayout.id, MyLibraryFragment()).addToBackStack(null)
            .commitAllowingStateLoss()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        Log.d("LibraryMainFragment", "onResume")
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

}