package com.mangpo.bookclub.view.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentAboutMeDialogBinding
import com.mangpo.bookclub.model.UserModel
import com.mangpo.bookclub.util.DialogFragmentUtils
import com.mangpo.bookclub.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class AboutMeDialogFragment : DialogFragment() {

    private val mainVm: MainViewModel by sharedViewModel()

    private lateinit var binding: FragmentAboutMeDialogBinding
    private lateinit var user: UserModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAboutMeDialogBinding.inflate(layoutInflater)

        //다이얼로그 프래그먼트 모서리 둥글게
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        setMyClickListener()
        observe()

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        //전체 프래그먼트 크기 설정
        DialogFragmentUtils.dialogFragmentResize(
            requireContext(),
            this@AboutMeDialogFragment,
            0.84f,
            0.2f
        )
    }

    private fun setUI() {
        //MyInfoActivity 로부터 전달받은 자기소개 글을 editText 에 보여주기
        binding.aboutMeEt.setText(user.introduce)
    }

    private fun setMyClickListener() {
        //프래그먼트 닫기 이미지뷰 클릭 리스너
        binding.closeIv.setOnClickListener {
            dismiss()
        }

        //완료 텍스트뷰 클릭 리스너 -> updateUser API 요청
        binding.completeTv.setOnClickListener {
            updateIntroduce(binding.aboutMeEt.text.toString())
        }
    }

    private fun updateIntroduce(introduce: String) {
        CoroutineScope(Dispatchers.IO).launch {
            user.introduce = introduce
            mainVm.updateUser(user)
        }
    }

    private fun observe() {
        mainVm.user.observe(this, Observer {
            Log.d("AboutMeDialogFragment", "user Observe!! -> $it")

            user = it
            setUI()
        })

        mainVm.updateUserCode.observe(this, Observer {
            Log.d("AboutMeDialogFragment", "updateUserCode Observe!! -> $it")

            if (it == 204) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.msg_update_introduce),
                    Toast.LENGTH_SHORT
                ).show()
                mainVm.setUser(user)
                dismiss()
                mainVm.setUpdateUserCode(-1)
            } else if (it != -1)
                Toast.makeText(requireContext(), getString(R.string.err_change), Toast.LENGTH_SHORT)
                    .show()

        })
    }
}