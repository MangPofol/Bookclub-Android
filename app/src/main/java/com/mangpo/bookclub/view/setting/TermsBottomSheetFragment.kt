package com.mangpo.bookclub.view.setting

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentTermsBottomSheetBinding

class TermsBottomSheetFragment(private val type: String) : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentTermsBottomSheetBinding

    companion object {
        fun newInstance(type: String): TermsBottomSheetFragment = TermsBottomSheetFragment(type)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TermsBottomSheetFragment", "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("TermsBottomSheetFragment", "onCreateView")
        binding = FragmentTermsBottomSheetBinding.inflate(inflater, container, false)

        //type 에 따라 제목과 본문을 다르게 바꾼다.
        when (type) {
            "service" -> {
                binding.titleTv.text = getString(R.string.terms_of_service)
                binding.contentTv.text = getString(R.string.terms_of_service_content)
            }
            "privacy" -> {
                binding.titleTv.text = getString(R.string.privacy_policy)
                binding.contentTv.text = getString(R.string.privacy_policy_content)
            }
            else -> {
                binding.titleTv.text = getString(R.string.receive_marketing_information)
                binding.contentTv.text = getString(R.string.receive_marketing_information_content)
            }
        }

        //확인 버튼 클릭 리스너 -> 프래그먼트 종료
        binding.confirmBtn.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    //다이얼로그 위 테두리가 둥글게 돼 있는 테마로 설정
    override fun getTheme(): Int {
        return R.style.BottomSheetDialog
    }
}