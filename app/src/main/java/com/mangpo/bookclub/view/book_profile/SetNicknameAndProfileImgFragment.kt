package com.mangpo.bookclub.view.book_profile

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentSetNicknameAndProfileImgBinding
import com.mangpo.bookclub.view.CameraGalleryBottomSheetFragment2

class SetNicknameAndProfileImgFragment : Fragment(), TextWatcher {

    private var profileImgUrl: String = ""
    private var isProfileImgSetting: Boolean = false

    private lateinit var binding: FragmentSetNicknameAndProfileImgBinding

    private val cameraGalleryBottomSheetFragment: CameraGalleryBottomSheetFragment2 = CameraGalleryBottomSheetFragment2.newInstance(1) {
        profileImgUrl = it[0]

        updateProfileImg()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSetNicknameAndProfileImgBinding.inflate(inflater, container, false)

        (activity as BookProfileInitActivity).invisibleSkipTV()
        (activity as BookProfileInitActivity).unEnableNextBtn()     //다음 버튼 비활성화

        binding.nicknameEt.addTextChangedListener(this) //텍스트 입력 시 이벤트 처리

        binding.addProfileIvView.setOnClickListener {
            cameraGalleryBottomSheetFragment.show(requireActivity().supportFragmentManager, null)
        }

        return binding.root
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (count == 0) { //입력값이 없으면 다음 버튼 비활성화
            (activity as BookProfileInitActivity).unEnableNextBtn()
        } else {
            if (isProfileImgSetting) //입력값이 있고 프로필 이미지를 설정했으면 다음 버튼 활성화
                (activity as BookProfileInitActivity).enableNextBtn()
            else    //입력값이 있고 프로필 이미지를 설정하지 않았으면 다음 버튼 비활성화
                (activity as BookProfileInitActivity).unEnableNextBtn()
        }
    }

    override fun afterTextChanged(s: Editable?) {

    }

    private fun updateProfileImg() {
        Glide.with(requireActivity().applicationContext).load(profileImgUrl).circleCrop().into(binding.profileIv)
        binding.plusIv.setImageResource(R.drawable.setting_icon_white)
        isProfileImgSetting = true
    }

    fun getNickname():String = binding.nicknameEt.text.toString()

    fun getProfileImg(): String = profileImgUrl
}