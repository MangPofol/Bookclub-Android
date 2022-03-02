package com.mangpo.ourpage.view.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.mangpo.ourpage.databinding.FragmentActionDialogBinding
import com.mangpo.ourpage.utils.DialogFragmentUtils

class ActionDialogFragment : DialogFragment() {
    interface MyDialogCallback {
        fun delete()
    }

    private lateinit var binding: FragmentActionDialogBinding
    private lateinit var myDialogCallback: MyDialogCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentActionDialogBinding.inflate(inflater, container, false)

        setMyClickListener()

        //다이얼로그 프래그먼트 모서리 둥글게
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        binding.actionDialogTitleTv.text = arguments?.getString("title")
        binding.actionDialogDescTv.text = arguments?.getString("desc")

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        //전체 프래그먼트 크기 설정
        DialogFragmentUtils.dialogFragmentResize(
            requireContext(),
            this@ActionDialogFragment,
            0.74f,
            0.15f
        )
    }

    private fun setMyClickListener() {
        binding.actionDialogCancelTv.setOnClickListener { dismiss() }

        binding.actionDialogDeleteTv.setOnClickListener {
            dismiss()
            myDialogCallback.delete()
        }
    }

    fun setMyDialogCallback(myDialogCallback: MyDialogCallback) {
        this.myDialogCallback = myDialogCallback
    }
}