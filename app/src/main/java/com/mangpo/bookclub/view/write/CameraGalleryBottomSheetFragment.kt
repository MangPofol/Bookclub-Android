package com.mangpo.bookclub.view.write

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentCameraGalleryBottomSheetBinding

class CameraGalleryBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentCameraGalleryBottomSheetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCameraGalleryBottomSheetBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun getTheme(): Int {
        return R.style.BottomSheetDialog
    }
}