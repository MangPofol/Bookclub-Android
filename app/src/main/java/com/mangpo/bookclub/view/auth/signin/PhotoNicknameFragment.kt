package com.mangpo.bookclub.view.auth.signin

import android.Manifest
import android.text.Editable
import android.text.TextWatcher
import com.bumptech.glide.Glide
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentPhotoNicknameBinding
import com.mangpo.bookclub.view.BaseFragment
import gun0912.tedimagepicker.builder.TedImagePicker

class PhotoNicknameFragment : BaseFragment<FragmentPhotoNicknameBinding>(FragmentPhotoNicknameBinding::inflate), TextWatcher {

    private lateinit var profileImg: String

    //퍼미션 확인 후 콜백 리스너
    private var permissionListener: PermissionListener = object : PermissionListener {
        override fun onPermissionGranted() {
            goGallery()
        }

        override fun onPermissionDenied(deniedPermissions: List<String>) {
        }
    }

    override fun initAfterBinding() {
        (requireActivity() as SignInActivity).changeToolbarText(null, null, getString(R.string.msg_input_information_display_my_profile))
        validate()
        setMyEventListener()
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        validate()
    }

    override fun afterTextChanged(p0: Editable?) {
    }

    private fun validate() {
        if (binding.photoNicknameNicknameEt.text.isBlank()) {
            (requireActivity() as SignInActivity).changeNextButtonState(false)
            (requireActivity() as SignInActivity).changeActionTextViewState(false)
        } else {
            (requireActivity() as SignInActivity).changeNextButtonState(true)
            (requireActivity() as SignInActivity).changeActionTextViewState(true)
        }
    }

    private fun setMyEventListener() {
        binding.photoNicknameNicknameEt.addTextChangedListener(this)

        binding.photoNicknameSelectPlusIv.setOnClickListener {
            checkPermission()
        }
    }

    //카메라, 저장소 퍼미션 확인
    private fun checkPermission() {
        TedPermission.create()
            .setPermissionListener(permissionListener)
            .setDeniedMessage(getString(R.string.error_permission_denied))
            .setPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .check();
    }

    private fun goGallery() {
        TedImagePicker.with(requireContext())
            .title(R.string.title_photos)
            .backButton(R.drawable.ic_back)
            .buttonBackground(R.color.white)
            .buttonTextColor(R.color.primary)
            .savedDirectoryName("Ourpage")
            .start { uri ->
                Glide.with(requireContext()).load(uri).circleCrop().into(binding.photoNicknameSelectProfileIv)
                profileImg = uri.toString()
            }
    }

    fun getProfileImg(): String? {
        return if (::profileImg.isInitialized)
            profileImg
        else
            null
    }

    fun getNickname(): String = binding.photoNicknameNicknameEt.text.toString()
}