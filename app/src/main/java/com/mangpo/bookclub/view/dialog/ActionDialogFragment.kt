package com.mangpo.bookclub.view.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentActionDialogBinding
import com.mangpo.bookclub.utils.DialogFragmentUtils

class ActionDialogFragment : DialogFragment() {
    interface MyDialogCallback {
        fun action1()
        fun action2()
    }

    private lateinit var binding: FragmentActionDialogBinding
    private lateinit var myDialogCallback: MyDialogCallback
    private lateinit var title: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentActionDialogBinding.inflate(inflater, container, false)

        setMyClickListener()

        //다이얼로그 프래그먼트 모서리 둥글게
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        title = arguments?.getString("title")!!
        binding.actionDialogTitleTv.text = title
        binding.actionDialogDescTv.text = arguments?.getString("desc")

        when (title) {
            getString(R.string.msg_complete_checklist) -> {
                binding.actionDialogCancelTv.text = getString(R.string.action_undo)
                binding.actionDialogActionTv.text = getString(R.string.action_confirm)
            }
            else -> {
                binding.actionDialogCancelTv.text = getString(R.string.action_cancel)
                binding.actionDialogActionTv.text = getString(R.string.action_delete)
            }
        }

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
        binding.actionDialogCancelTv.setOnClickListener {
            dismiss()

            if (title==getString(R.string.msg_complete_checklist))
                myDialogCallback.action2()
        }

        binding.actionDialogActionTv.setOnClickListener {
            dismiss()
            myDialogCallback.action1()
        }
    }

    fun setMyDialogCallback(myDialogCallback: MyDialogCallback) {
        this.myDialogCallback = myDialogCallback
    }
}