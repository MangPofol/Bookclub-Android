package com.mangpo.bookclub.view.main

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.viewpager.widget.ViewPager
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.ActivityMainBinding
import com.mangpo.bookclub.view.adapter.BottomNavigationPagerAdapter
import com.mangpo.bookclub.view.write.PostFragment
import com.mangpo.bookclub.view.write.RecordFragment
import com.mangpo.bookclub.viewmodel.BookViewModel
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mDrawerToggle: ActionBarDrawerToggle
    private lateinit var mPreferences: SharedPreferences

    private var email: String = ""
    private var bottomNavigationPagerAdapter: BottomNavigationPagerAdapter =
        BottomNavigationPagerAdapter(supportFragmentManager)

    private val bookViewModel: BookViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mPreferences = getSharedPreferences("emailPreferences", AppCompatActivity.MODE_PRIVATE)
        email = mPreferences.getString("email", "")!!

        initBookList()

        //뷰페이저 어댑터 설정&페이지 변환 리스너 설정
        binding.bottomViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(    //뷰페이저 스크롤 시 프래그먼트 전환되면서 bottom navigation 아이콘 체크 상태 변경
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                binding.bottomNavigation.menu.getItem(position).isChecked = true
            }

            override fun onPageSelected(position: Int) {

            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

        //bottom navigation 메뉴 선택 시 프래그먼트 전환
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.write -> {
                    binding.bottomViewPager.currentItem = 0

                    return@setOnItemSelectedListener true
                }
                R.id.library -> {
                    binding.bottomViewPager.currentItem = 1

                    return@setOnItemSelectedListener true
                }
                R.id.myBookclub -> {
                    binding.bottomViewPager.currentItem = 2

                    return@setOnItemSelectedListener true
                }
            }

            return@setOnItemSelectedListener false
        }
    }

    override fun onBackPressed() {
        //drawer layout이 열려 있는 상태면 -> drawer layout 부터 닫는다.
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START))
            binding.drawerLayout.closeDrawers()
        else if (supportFragmentManager.fragments[0].javaClass == PostFragment::class.java && supportFragmentManager.fragments[0].childFragmentManager.fragments[0].javaClass != RecordFragment::class.java) {
            supportFragmentManager.fragments[0].childFragmentManager.popBackStackImmediate()
            (supportFragmentManager.fragments[0] as PostFragment).moveToRecord()
        } else {
            super.onBackPressed()
            supportFragmentManager.popBackStackImmediate()
        }

    }

    private fun setBottom() {
        binding.bottomViewPager.adapter = bottomNavigationPagerAdapter
        binding.bottomNavigation.selectedItemId = R.id.write
        binding.bottomViewPager.currentItem = 0
    }

    private fun initBookList() {
        CoroutineScope(Dispatchers.Main).launch {
            bookViewModel.requestBookList(email, "NOW")
            bookViewModel.requestBookList(email, "AFTER")
            bookViewModel.requestBookList(email, "BEFORE")

            setBottom()
        }
    }

    //navigation drawer 설정
    fun setDrawer(toolbar: Toolbar) {
        var activity = MainActivity()

        mDrawerToggle = ActionBarDrawerToggle(
            activity,
            binding.drawerLayout,
            toolbar,
            R.string.open,
            R.string.close
        )
        mDrawerToggle!!.syncState()
    }

    fun moveBottomPager(currentItem: Int) {
        binding.bottomViewPager.currentItem = currentItem
    }

    //올라와 있는 키보드를 내리는 함수
    fun hideKeyBord(v: View) {
        val imm: InputMethodManager =
            getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }

    fun goBookDesc(bookId: Long, bookName: String) {
        binding.bottomViewPager.currentItem = 0
        (supportFragmentManager.fragments[0] as PostFragment).moveToPostDetail(bookId, bookName)
    }

    fun getEmail(): String = email

    fun backFragment() {
        Log.d("MainActivity", supportFragmentManager.fragments.toString())
    }
}