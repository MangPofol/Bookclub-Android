package com.mangpo.bookclub.view.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.gson.Gson
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.ActivityMainBinding
import com.mangpo.bookclub.model.BookModel
import com.mangpo.bookclub.model.PostModel
import com.mangpo.bookclub.model.UserModel
import com.mangpo.bookclub.util.BackStackManager
import com.mangpo.bookclub.view.library.BookDescFragment
import com.mangpo.bookclub.view.my_info.ChecklistManagementActivity
import com.mangpo.bookclub.view.my_info.GoalManagementActivity
import com.mangpo.bookclub.view.my_info.MyInfoActivity
import com.mangpo.bookclub.view.write.PostDetailFragment
import com.mangpo.bookclub.viewmodel.BookViewModel
import com.mangpo.bookclub.viewmodel.PostViewModel
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var user: UserModel

    private var isMenuItemSelectListenerEnable: Boolean =
        false  //bottom navigation menu select 이벤트 리스너를 실행할건지 체크하기 위한 변수

    private val bookVm: BookViewModel by viewModel()
    private val postVm: PostViewModel by viewModel()
    private val bookBundle: Bundle = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = Gson().fromJson(intent.getStringExtra("user"), UserModel::class.java)
        initBookList()

        //bottom navigation 메뉴 선택 시 프래그먼트 전환
        binding.bottomNavigation.setOnItemSelectedListener {
            if (isMenuItemSelectListenerEnable) {
                isMenuItemSelectListenerEnable = false
            } else {
                val fm = supportFragmentManager
                val transaction = fm.beginTransaction()

                when (it.itemId) {
                    R.id.write -> {
                        val fragment = BackStackManager.switchFragment(0)
                        transaction.replace(binding.frameLayout.id, fragment)
                            .commitAllowingStateLoss()
                    }
                    R.id.library -> {
                        val fragment = BackStackManager.switchFragment(1)
                        transaction.replace(binding.frameLayout.id, fragment)
                            .commitAllowingStateLoss()

                        postVm.setPost(PostModel())
                        postVm.setImgUriList(listOf())
                        bookVm.setBook(BookModel())
                    }
                    R.id.myBookclub -> {
                        val fragment = BackStackManager.switchFragment(2)
                        transaction.replace(binding.frameLayout.id, fragment)
                            .commitAllowingStateLoss()
                    }
                }

                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            }

            return@setOnItemSelectedListener true
        }

        binding.bottomNavigation.selectedItemId = R.id.write
    }

    override fun onResume() {
        super.onResume()
        Log.d("MainActivity", "onResume")

    }

    override fun onBackPressed() {
        var fragment = BackStackManager.popFragment()

        if (fragment == null)
            finishAffinity()
        //pop 된 프래그먼트랑 현재 프래그먼트랑 같은 경우가 있어서 그럴 땐 프래그먼트를 한번 더 pop 한다.
        else if (supportFragmentManager.findFragmentById(binding.frameLayout.id)?.javaClass == fragment.javaClass) {
            fragment = BackStackManager.popFragment()
        }

        //pop 이후에 fragment 가 null 이 되는 경우가 발생 -> 그럴 땐 종료하면 됨.
        if (fragment == null) {
            BackStackManager.clear()
            finishAffinity()
        } else {
            supportFragmentManager.beginTransaction().replace(binding.frameLayout.id, fragment)
                .commitAllowingStateLoss()
            changeBottomNavigation(BackStackManager.getMenu())
        }
    }

    fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(binding.frameLayout.id, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commitAllowingStateLoss()
    }

    fun initBookList() {
        CoroutineScope(Dispatchers.Main).launch {
            bookVm.requestBookList(user.email!!, "NOW")
            bookVm.requestBookList(user.email!!, "AFTER")
            bookVm.requestBookList(user.email!!, "BEFORE")
        }
    }

    fun changeBottomNavigation(menu: Int) {
        isMenuItemSelectListenerEnable = true

        when (menu) {
            0 -> binding.bottomNavigation.selectedItemId = R.id.write
            1 -> binding.bottomNavigation.selectedItemId = R.id.library
            else -> binding.bottomNavigation.selectedItemId = R.id.myBookclub
        }
    }

    //올라와 있는 키보드를 내리는 함수
    fun hideKeyBord(v: View) {
        val imm: InputMethodManager =
            getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }

    fun getEmail(): String = user.email!!

    fun moveToBookDesc(book: BookModel) {
        val fragment = BookDescFragment().apply {
            arguments = Bundle().apply {
                putString("book", Gson().toJson(book))
            }
        }
        supportFragmentManager.beginTransaction()
            .replace(binding.frameLayout.id, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commitAllowingStateLoss()

        BackStackManager.pushFragment(1, fragment)
        BackStackManager.pushFragment(1, fragment)
    }

    fun moveToPostDetail(book: BookModel) {
        bookBundle.putString("book", Gson().toJson(book))

        val fragment = PostDetailFragment().apply {
            arguments = Bundle().apply {
                putLong("bookId", book.id!!)
                putString("bookName", book.name)
            }
        }
        changeFragment(fragment)
        BackStackManager.pushFragment(0, fragment)
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