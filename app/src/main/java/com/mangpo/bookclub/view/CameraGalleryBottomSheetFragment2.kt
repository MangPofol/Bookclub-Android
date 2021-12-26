package com.mangpo.bookclub.view

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentCameraGalleryBottomSheetBinding
import com.mangpo.bookclub.view.write.CameraActivity
import gun0912.tedimagepicker.builder.TedImagePicker
import gun0912.tedimagepicker.builder.type.MediaType
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class CameraGalleryBottomSheetFragment2(val imgCnt: Int, val callback: (List<String>) -> Unit) :
    BottomSheetDialogFragment() {

    private lateinit var binding: FragmentCameraGalleryBottomSheetBinding
    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>

    private var cameraPermissionLauncher: ActivityResultLauncher<String>? = null
    private var galleryPermissionLauncher: ActivityResultLauncher<Array<String>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("CameraGalleryBottomSheetFragment2", "onCreate")

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
                if (result.resultCode == Activity.RESULT_OK) {
                    val uri = Uri.parse(result.data?.getStringExtra("uri"))
                    val imgAbsolutePath = getAbsolutePathByBitmap(uriToBitmap(uri))
                    callback(listOf(imgAbsolutePath))
                    dismiss()
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("CameraGalleryBottomSheetFragment2", "onCreateView")
        binding = FragmentCameraGalleryBottomSheetBinding.inflate(inflater, container, false)

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
        val pictures = arrayListOf<Uri>()

        if (imgCnt == 1) {
            TedImagePicker.with(requireContext())
                .mediaType(MediaType.IMAGE)
                .cameraTileBackground(R.color.grey1)
                .title(R.string.gallery_title)
                .backButton(R.drawable.back_icon)
                .buttonBackground(R.color.white)
                .buttonTextColor(R.color.main_blue)
                .dropDownAlbum()
                .start { uri ->
                    val imgAbsolutePath = getAbsolutePathByBitmap(uriToBitmap(uri))
                    callback(listOf(imgAbsolutePath))
                    dismiss()
                }
        } else {
            TedImagePicker.with(requireContext())
                .mediaType(MediaType.IMAGE)
                .cameraTileBackground(R.color.grey1)
                .title(R.string.gallery_title)
                .max(4, R.string.max_image_desc)
                .backButton(R.drawable.back_icon)
                .buttonBackground(R.color.white)
                .buttonTextColor(R.color.main_blue)
                .dropDownAlbum()
                .startMultiImage { uriList ->
                    val imgAbsolutePaths = arrayListOf<String>()
                    for (uri in uriList) {
                        imgAbsolutePaths.add(getAbsolutePathByBitmap(uriToBitmap(uri)))
                    }
                    callback(imgAbsolutePaths)
                    dismiss()
                }
        }

    }

    private fun uriToBitmap(uri: Uri): Bitmap {
        val bitmap = if (Build.VERSION.SDK_INT < 28) {
            MediaStore.Images.Media.getBitmap(
                requireActivity().contentResolver,
                uri
            )
        } else {
            val source = ImageDecoder.createSource(requireActivity().contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        }

        return bitmap
    }

    private fun getAbsolutePathByBitmap(bitmap: Bitmap): String {
        val path =
            (requireContext().applicationInfo.dataDir + File.separator + System.currentTimeMillis())
        val file = File(path)
        var out: OutputStream? = null

        try {
            file.createNewFile()
            out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, out)
        } finally {
            out?.close()
        }

        return file.absolutePath
    }
}