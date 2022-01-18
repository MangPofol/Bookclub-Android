package com.mangpo.bookclub.view.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.mangpo.bookclub.databinding.FragmentToastDialogBinding
import com.mangpo.bookclub.util.DialogFragmentUtils

class ToastDialogFragment(private val msg: String) : DialogFragment() {
    private lateinit var binding: FragmentToastDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentToastDialogBinding.inflate(inflater, container, false)

        //다이얼로그 프래그먼트 모서리 둥글게
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        binding.msgTv.text = msg

        binding.confirmTv.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        //전체 프래그먼트 크기 설정
        DialogFragmentUtils.dialogFragmentResize(
            requireContext(),
            this@ToastDialogFragment,
            0.74f,
            0.18f
        )
    }
}