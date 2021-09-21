package com.example.bookclub.view.bookclub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.bookclub.R
import com.example.bookclub.databinding.ActivityCreateClubBinding
import com.example.bookclub.model.ClubModel
import com.example.bookclub.viewmodel.ClubViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreateClubActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateClubBinding
    private val clubViewModel: ClubViewModel by viewModels<ClubViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateClubBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var colorSet: String = "A"  //클럽 colorSet

        //시스템 툴바 보이도록
        val window = window
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
        )

        //확인 버튼 누르면 -> 서버에 클럽 생성 요청 보내기
        binding.confirmTV.setOnClickListener {
            val clubName: String = binding.clubNameET.text.toString()
            val clubInfo: String = binding.clubInfoET.text.toString()
            val newClub: ClubModel = ClubModel(name=clubName, description=clubInfo, colorSet=colorSet)

            CoroutineScope(Dispatchers.IO).launch {
                val code = clubViewModel.createClub(newClub)
                Log.e("CreateClubActivity-클럽 생성", code.toString())
            }
        }

        //club colorSet 이미지뷰 클릭할 때마다 colorSet 데이터가 변경되도록
        binding.clubPatternIV1.setOnClickListener {
            colorSet = "A"
        }
        binding.clubPatternIV2.setOnClickListener {
            colorSet = "B"
        }
        binding.clubPatternIV3.setOnClickListener {
            colorSet = "C"
        }
    }

}