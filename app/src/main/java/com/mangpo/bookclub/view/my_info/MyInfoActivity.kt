package com.mangpo.bookclub.view.my_info

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.ActivityMyInfoBinding
import com.mangpo.bookclub.model.UserModel
import com.mangpo.bookclub.view.CameraGalleryBottomSheetFragment2
import com.mangpo.bookclub.viewmodel.MainViewModel
import com.mangpo.bookclub.viewmodel.PostViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyInfoActivity : AppCompatActivity() {

    private val mainVm: MainViewModel by viewModel()
    private val postVm: PostViewModel by viewModel()
    private val cameraGalleryBottomSheetFragment: CameraGalleryBottomSheetFragment2 =
        CameraGalleryBottomSheetFragment2.newInstance(1) {
            profileImgCallback(it[0])
        }

    private var beforeProfileImg: String? = null

    private lateinit var binding: ActivityMyInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MyInfoActivity", "onCreate")

        binding = ActivityMyInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observe()

        getUser()

        //프로필 이미지 설정 클릭 리스너
        binding.profileImageSettingView.setOnClickListener {
            //카메라 or 갤러리 선택하는 bottom dialog 실행
            cameraGalleryBottomSheetFragment.show(supportFragmentManager, null)
        }

        binding.genreNextIv.setOnClickListener {
            goYourTaste()
        }

        binding.styleNextIv.setOnClickListener {
            goYourTaste()
        }

        binding.goalManagementIv.setOnClickListener {
            goGoalManagement()
        }

        binding.checklistManagementIv.setOnClickListener {
            goChecklistManagement()
        }

        binding.backIvView.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("MyInfoActivity", "onResume")

        getUser()
    }

    private fun getUser() {
        CoroutineScope(Dispatchers.IO).launch {
            mainVm.getUser()
        }
    }

    private fun setUI(user: UserModel) {
        binding.nicknameTv.text = user.nickname
        binding.idTv.text = user.email

        if (user.introduce == "")
            binding.introduceTv.text = "나를 한 줄로 표현해보세요."
        else
            binding.introduceTv.text = user.introduce

        if (user.genres.isEmpty()) {
            binding.genreCg.removeAllViews()
            binding.genreCg.visibility = View.INVISIBLE
            binding.selectGenreTv.visibility = View.VISIBLE
        } else {
            binding.genreCg.visibility = View.VISIBLE
            binding.selectGenreTv.visibility = View.INVISIBLE
            addGenreChip(user.genres)
        }

        if (user.profileImgLocation == "")
            Glide.with(applicationContext).load(R.drawable.no_profile_img).into(binding.profileIv)
        else
            Glide.with(applicationContext).load(user.profileImgLocation).circleCrop().into(binding.profileIv)
    }

    private fun addGenreChip(genres: List<String>) {
        binding.genreCg.removeAllViews()

        for (genre in genres) {
            val chip: Chip =
                layoutInflater.inflate(R.layout.my_info_genre_chip, binding.genreCg, false) as Chip
            chip.text = genre
            binding.genreCg.addView(chip)
        }
    }

    //ChecklistManagementActivity 화면으로 이동하는 함수
    private fun goChecklistManagement() {
        startActivity(Intent(this, ChecklistManagementActivity::class.java))
    }

    //GoalManagementActivity 화면으로 이동하는 함수
    private fun goGoalManagement() {
        startActivity(Intent(this, GoalManagementActivity::class.java))
    }

    //YourTasteActivity 화면으로 이동하는 함수
    private fun goYourTaste() {
        startActivity(Intent(this, YourTasteActivity::class.java))
    }

    //bottom sheet dialog 를 통해 얻어온 callback 함수
    private fun profileImgCallback(profileImg: String) {
        updateProfileImg(profileImg)
    }

    //프로필 이미지 업데이트 함수
    private fun updateProfileImg(profileImg: String) {
        CoroutineScope(Dispatchers.Main).launch {
            val url = postVm.uploadImg(profileImg)

            if (url != null) {
                val user = mainVm.user.value
                beforeProfileImg = user!!.profileImgLocation //업데이트 된 프로필 이미지로 변경하기 전에 이전 프로필 이미지 저장.
                user!!.profileImgLocation = url
                mainVm.updateUser(user)
            } else {
                Toast.makeText(
                    this@MyInfoActivity,
                    "이미지 업로드 중 오류 발생. 다시 시도해 주세요.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    //이전 프로필 이미지 삭제하는 함수
    private fun deleteProfileImg() {
        CoroutineScope(Dispatchers.IO).launch {
            postVm.deleteImg(beforeProfileImg!!)
            beforeProfileImg = null //삭제가 됐던 안됐던 우선 프로필 이미지 변수 null 로 변경하기
        }
    }

    private fun observe() {
        mainVm.user.observe(this, Observer {
            setUI(it)

            //프로필 이미지가 업데이트 됐으면 이전의 프로필 이미지 삭제
            if (beforeProfileImg!=null)
                deleteProfileImg()
        })
    }

}