package com.mangpo.bookclub.view.main

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.ActivityMainBinding
import com.mangpo.bookclub.model.BookModel
import com.mangpo.bookclub.model.PostModel
import com.mangpo.bookclub.view.bookclub.BookClubFragment
import com.mangpo.bookclub.view.library.LibraryInitFragment
import com.mangpo.bookclub.view.library.MyLibraryFragment
import com.mangpo.bookclub.view.my_info.ChecklistManagementActivity
import com.mangpo.bookclub.view.my_info.GoalManagementActivity
import com.mangpo.bookclub.view.my_info.MyInfoActivity
import com.mangpo.bookclub.view.write.WriteFrameFragment
import com.mangpo.bookclub.view.write.WriteInitFragment
import com.mangpo.bookclub.viewmodel.BookViewModel
import com.mangpo.bookclub.viewmodel.PostViewModel
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mPreferences: SharedPreferences

    private val writeFrameFragment: WriteFrameFragment = WriteFrameFragment()
    private val libraryInitFragment: LibraryInitFragment = LibraryInitFragment()
    private val bookClubFragment: BookClubFragment = BookClubFragment()

    private var email: String = ""
    private var latestFragment: String = ""

    private val bookVm: BookViewModel by viewModel()
    private val postVm: PostViewModel by viewModel()
    private val bookBundle: Bundle = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mPreferences = getSharedPreferences("emailPreferences", AppCompatActivity.MODE_PRIVATE)
        email = mPreferences.getString("email", "")!!

        initBookList()

        //bottom navigation 메뉴 선택 시 프래그먼트 전환
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.write -> {
                    if (bookBundle.getString("book")==null)
                        writeFrameFragment.arguments = bookBundle

                    supportFragmentManager.beginTransaction().replace(binding.frameLayout.id, writeFrameFragment).addToBackStack("post").commitAllowingStateLoss()

                    return@setOnItemSelectedListener true
                }
                R.id.library -> {
                    supportFragmentManager.beginTransaction().replace(binding.frameLayout.id, libraryInitFragment).addToBackStack("libraryMain").commitAllowingStateLoss()
                    postVm.setPost(PostModel())
                    postVm.setImgUriList(listOf())
                    bookVm.setBook(BookModel())

                    return@setOnItemSelectedListener true
                }
                R.id.myBookclub -> {
                    supportFragmentManager.beginTransaction().replace(binding.frameLayout.id, bookClubFragment).addToBackStack("bookClub").commitAllowingStateLoss()
                    postVm.setPost(PostModel())
                    postVm.setImgUriList(listOf())
                    bookVm.setBook(BookModel())

                    return@setOnItemSelectedListener true
                }
            }

            return@setOnItemSelectedListener false
        }

        binding.bottomNavigation.selectedItemId = R.id.write
    }

    override fun onResume() {
        super.onResume()
        Log.d("MainActivity", "onResume")

    }

    override fun onBackPressed() {
        if (binding.bottomNavigation.selectedItemId == R.id.write) {
            val fragments = writeFrameFragment.childFragmentManager.fragments

            if (fragments[fragments.size-1].javaClass==WriteInitFragment::class.java)
                finish()
            else
                writeFrameFragment.childFragmentManager.popBackStackImmediate()
        } else if (binding.bottomNavigation.selectedItemId == R.id.library) {
            val fragments = libraryInitFragment.childFragmentManager.fragments
            if (fragments[fragments.size-1].javaClass == MyLibraryFragment::class.java)
                binding.bottomNavigation.selectedItemId = R.id.write
            else
                libraryInitFragment.childFragmentManager.popBackStackImmediate()
        } else {
            Log.d("MainActivity", "onBackPressed-BookClub -> ${bookClubFragment.childFragmentManager.fragments}")
        }
    }

    private fun initBookList() {
        CoroutineScope(Dispatchers.Main).launch {
            bookVm.requestBookList(email, "NOW")
            bookVm.requestBookList(email, "AFTER")
            bookVm.requestBookList(email, "BEFORE")
        }
    }

    //올라와 있는 키보드를 내리는 함수
    fun hideKeyBord(v: View) {
        val imm: InputMethodManager =
                getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }

    fun getEmail(): String = email

    fun setLatestFragment(name: String) {
        latestFragment = name
    }

    fun moveToBookDesc(book: BookModel) {
        libraryInitFragment.moveToBookDesc(book)
    }

    fun setBookBundle(book: BookModel) {
        bookBundle.putString("book", Gson().toJson(book))
    }

    fun changeBottomNavigation(menu: Int) {
        when (menu) {
            0 -> binding.bottomNavigation.selectedItemId = R.id.write
            1 -> binding.bottomNavigation.selectedItemId = R.id.library
            else -> binding.bottomNavigation.selectedItemId = R.id.myBookclub
        }
    }

    //ChecklistManagementActivity 화면으로 이동하는 함수
    fun goChecklistManagement() {
        startActivity(Intent(this, ChecklistManagementActivity::class.java))
    }

    //GoalManagementActivity 화면으로 이동하는 함수
    fun goGoalManagement() {
        startActivity(Intent(this, GoalManagementActivity::class.java))
    }

    //MyInfoActivity 화면으로 이동하는 함수
    fun goMyInfo() {
        startActivity(Intent(this, MyInfoActivity::class.java))
    }
}