package com.mangpo.bookclub.view.write

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentCameraGalleryBottomSheetBinding
import com.mangpo.bookclub.viewmodel.PostViewModel
import gun0912.tedimagepicker.builder.TedImagePicker
import gun0912.tedimagepicker.builder.type.MediaType
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import kotlin.collections.ArrayList

class CameraGalleryBottomSheetFragment() : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentCameraGalleryBottomSheetBinding
    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>

    private val postVm: PostViewModel by sharedViewModel()
    private var pictures: ArrayList<Uri> = ArrayList<Uri>()   //사진 찍거나 갤러리에서 선택한 이미지를 담는 리스트

    private var cameraPermissionLauncher: ActivityResultLauncher<String>? = null
    private var galleryPermissionLauncher: ActivityResultLauncher<Array<String>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cameraPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                if (it) {
                    val intent: Intent = Intent(requireContext(), CameraActivity::class.java)
                    cameraLauncher.launch(intent)
                } else {
                    Toast.makeText(requireContext(), "설정에서 카메라 권한을 승인해주세요", Toast.LENGTH_SHORT)
                        .show()
                }
            }

        galleryPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                if (it["android.permission.READ_EXTERNAL_STORAGE"]!! && it["android.permission.WRITE_EXTERNAL_STORAGE"]!!) {
                    goToGallery()
                } else {
                    Toast.makeText(requireContext(), "설정에서 갤러리 권한을 승인해주세요", Toast.LENGTH_SHORT)
                        .show()
                }
            }

        //카메라 런처
        cameraLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val uri = Uri.parse(result.data?.getStringExtra("uri"))
                    pictures.add(uri)
                    postVm.setImgUriList(pictures)
                    dismiss()
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCameraGalleryBottomSheetBinding.inflate(inflater, container, false)

        observe()

        //카메라를 선택하면 카메라 권한을 확인 or 요청하는 런처를 실행한다.
        binding.cameraIv.setOnClickListener {
            cameraPermissionLauncher!!.launch(android.Manifest.permission.CAMERA)
        }
        binding.cameraTv.setOnClickListener {
            cameraPermissionLauncher!!.launch(android.Manifest.permission.CAMERA)
        }

        //갤러리를 선택하면 갤러리 권한을 확인 or 요청하는 런처를 실행한다.
        binding.galleryIv.setOnClickListener {
            galleryPermissionLauncher!!.launch(
                arrayOf(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )
        }
        binding.galleryTv.setOnClickListener {
            galleryPermissionLauncher!!.launch(
                arrayOf(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )
        }

        return binding.root
    }

    //다이얼로그 위 테두리가 둥글게 돼 있는 테마로 설정
    override fun getTheme(): Int {
        return R.style.BottomSheetDialog
    }

    //이미지 선택하는 화면으로 이동하는 함수
    private fun goToGallery() {
        TedImagePicker.with(requireContext())
            .mediaType(MediaType.IMAGE)
            .cameraTileBackground(R.color.grey1)
            .title(R.string.gallery_title)
            .backButton(R.drawable.back_icon)
            .max(4, R.string.max_image_desc)
            .buttonBackground(R.color.white)
            .buttonTextColor(R.color.main_blue)
            .dropDownAlbum()
            .startMultiImage { uriList ->
                for (uri in uriList) {
                    pictures.add(uri)
                }
                postVm.setImgUriList(pictures)
                dismiss()
            }
    }

    private fun observe() {
        postVm.imgUriList.observe(viewLifecycleOwner, Observer {
            pictures = it as ArrayList<Uri>
        })
    }
}