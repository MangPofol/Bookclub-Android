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
import com.mangpo.bookclub.databinding.FragmentRemoveDialogBinding
import com.mangpo.bookclub.util.DialogFragmentUtils

class RemoveDialogFragment(private val msg: String, val callback: (Boolean) -> Unit) :
    DialogFragment() {
    private lateinit var binding: FragmentRemoveDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRemoveDialogBinding.inflate(inflater, container, false)

        setUI()

        //취소 텍스트뷰 클릭 리스너 -> 프래그먼트 닫기, 콜백 함수에 삭제 안한다는(false) 데이터 전달
        binding.cancelTv.setOnClickListener {
            dismiss()
            callback(false)
        }

        //삭제 텍스트뷰 클릭 리스너 -> 프래그먼트 닫기, 콜백 함수에 삭제 한다는(true) 데이터 전달
        binding.deleteTv.setOnClickListener {
            dismiss()
            callback(true)
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        //전체 프래그먼트 크기 설정
        DialogFragmentUtils.dialogFragmentResize(
            requireContext(),
            this@RemoveDialogFragment,
            0.74f,
            0.18f
        )
    }

    private fun setUI() {
        //다이얼로그 프래그먼트 모서리 둥글게
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        binding.titleTv.text = msg

        if (msg == getString(R.string.title_delete_book)) {
            binding.msgTv.text = getString(R.string.msg_memo_list_all_delete)
        } else {
            binding.msgTv.text = getString(R.string.msg_delete_memo)
        }
    }
}