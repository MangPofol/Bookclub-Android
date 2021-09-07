package com.example.bookclub.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.example.bookclub.R
import com.example.bookclub.view.adapter.BottomNavigationPagerAdapter
import com.example.bookclub.databinding.ActivityMainBinding
import com.example.bookclub.model.UserModel
import com.example.bookclub.view.bookclub.CreateClubActivity
import com.example.bookclub.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mDrawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val window = window
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //navigation drawer 화면 메뉴 아이템 클릭 리스너
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            // Handle menu item selected
            when(menuItem.itemId) {
                R.id.myBookclub -> {
                    val intent: Intent = Intent(this@MainActivity, CreateClubActivity::class.java)
                    startActivity(intent)
                }
            }

            true
        }

        binding.bottomViewPager.adapter = BottomNavigationPagerAdapter(supportFragmentManager)
        binding.bottomViewPager.currentItem = 1
        binding.bottomNavigation.selectedItemId = R.id.library

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

    override fun onBackPressed() {
        //view pager의 현재 프래그먼트 가져오기
        var fragment: Fragment = binding.bottomViewPager.adapter!!.instantiateItem(
            binding.bottomViewPager,
            binding.bottomViewPager.currentItem
        ) as Fragment

        //drawer layout이 열려 있는 상태면 -> drawer layout 부터 닫는다.
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START))
            binding.drawerLayout.closeDrawers()
        else {
            super.onBackPressed()
        }

    }
}