package com.mangpo.bookclub.view.bookclub

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import com.mangpo.bookclub.databinding.ActivityCreateClubBinding
import com.mangpo.bookclub.model.ClubModel
import com.mangpo.bookclub.viewmodel.ClubViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
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

        //뒤로가는 화살표 아이콘 누르면 이전 화면(내 서재)로 이동.
        binding.topAppBar.setNavigationOnClickListener {
            Log.e("CreateClubActivity", "뒤로가기 버튼 클릭")
            finish()
        }

        //확인 버튼 누르면 -> 서버에 클럽 생성 요청 보내기
        binding.confirmTV.setOnClickListener {
            val clubName: String = binding.clubNameET.text.toString()
            val clubInfo: String = binding.clubInfoET.text.toString()

            when {
                clubName == "" -> {   //클럽명이 적혀 있지 않을 때
                    Toast.makeText(this@CreateClubActivity, "클럽명을 입력해주세요", Toast.LENGTH_SHORT)
                        .show()
                }
                clubInfo == "" -> {   //클럽 한줄 소개가 적혀 있지 않을 때
                    Toast.makeText(this@CreateClubActivity, "클럽 한줄 소개를 입력해주세요", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {   //클럽명 생성 요청을 서버에 전달한다.
                    var newClub: ClubModel =
                        ClubModel(name = clubName, description = clubInfo, colorSet = colorSet)

                    CoroutineScope(Dispatchers.Main).launch {
                        val res = async(Dispatchers.IO) {
                            clubViewModel.createClub(newClub)   //서버 요청
                        }

                        when (res.await().code()) {
                            201 -> {    //CreateClubContract 에 새로 생성된 club 데이터를 전달한다.
                                newClub = res.await().body()!!
                                val bundle: Bundle = Bundle()
                                bundle.putLong("id", newClub.id!!)
                                bundle.putString("name", newClub.name)
                                bundle.putString("colorSet", newClub.colorSet)
                                bundle.putInt("level", newClub.level)
                                bundle.putLong("presidentId", newClub.presidentId!!)
                                bundle.putString("description", newClub.description)
                                bundle.putString("createdDate", newClub.createdDate)
                                bundle.putString("modifiedDate", newClub.modifiedDate)

                                setResult(
                                    Activity.RESULT_OK,
                                    Intent().apply { putExtra("newClub", bundle) })

                                finish()
                            }
                            400 -> {    //중복 클럽명이 존재하는 상황
                                Toast.makeText(
                                    this@CreateClubActivity,
                                    "이미 사용 중인 클럽명입니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
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

    override fun onPause() {
        super.onPause()
        Log.e("createClubActivity", "onPause")
    }

}