package com.mangpo.bookclub.view.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentIntroduceDialogBinding
import com.mangpo.bookclub.utils.DialogFragmentUtils

class IntroduceDialogFragment : DialogFragment() {
    interface MyDialogCallback {
        fun getIntroduce(introduce: String)
    }

    private lateinit var binding: FragmentIntroduceDialogBinding
    private lateinit var myDialogCallback: MyDialogCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIntroduceDialogBinding.inflate(inflater, container, false)

        //다이얼로그 프래그먼트 모서리 둥글게
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setMyEventListener()

        //전체 프래그먼트 크기 설정
        DialogFragmentUtils.dialogFragmentResize(
            requireContext(),
            this@IntroduceDialogFragment,
            0.84f,
            0.2f
        )

        binding.introduceDialogEditEt.setText(arguments?.getString("introduce"))
    }

    private fun setMyEventListener() {
        binding.introduceDialogCloseIv.setOnClickListener {
            dismiss()
        }

        binding.introduceDialogCompleteTv.setOnClickListener {
            if (binding.introduceDialogEditEt.text.toString().isBlank())
                Toast.makeText(requireContext(), getString(R.string.msg_input_content), Toast.LENGTH_SHORT).show()
            else
                myDialogCallback.getIntroduce(binding.introduceDialogEditEt.text.toString())
        }
    }

    fun setMyDialogCallback(myDialogCallback: MyDialogCallback) {
        this.myDialogCallback = myDialogCallback
    }
}