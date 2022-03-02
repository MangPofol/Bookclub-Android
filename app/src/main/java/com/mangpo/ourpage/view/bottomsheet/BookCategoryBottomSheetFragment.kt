package com.mangpo.ourpage.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mangpo.ourpage.R
import com.mangpo.ourpage.databinding.FragmentBookCategoryBottomSheetBinding

class BookCategoryBottomSheetFragment : BottomSheetDialogFragment() {
    interface MyDialogCallback {
        fun getCategory(category: String)
    }

    private lateinit var binding: FragmentBookCategoryBottomSheetBinding
    private lateinit var myDialogCallback: MyDialogCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookCategoryBottomSheetBinding.inflate(inflater, container, false)

        binding.bookCategoryBtn.setOnClickListener {
            when (binding.bookCategoryRg.checkedRadioButtonId) {
                R.id.book_category_reading_rb -> {
                    myDialogCallback.getCategory("NOW")
                    dismiss()
                }
                R.id.book_category_read_complete_rb -> {
                    myDialogCallback.getCategory("AFTER")
                    dismiss()
                }
                R.id.book_category_read_before_rb -> {
                    myDialogCallback.getCategory("BEFORE")
                    dismiss()
                }
                else -> Toast.makeText(requireContext(), getString(R.string.msg_select_category), Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    override fun getTheme(): Int {
        return R.style.BottomSheetDialog
    }

    fun setMyDialogCallback(myDialogCallback: MyDialogCallback) {
        this.myDialogCallback = myDialogCallback
    }
}