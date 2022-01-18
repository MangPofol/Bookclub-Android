package com.mangpo.bookclub.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.gson.Gson
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.ActivityMainBinding
import com.mangpo.bookclub.model.BookModel
import com.mangpo.bookclub.model.PostDetailModel
import com.mangpo.bookclub.util.BackStackManager
import com.mangpo.bookclub.util.OnBackPressedListener
import com.mangpo.bookclub.view.library.BookDetailFragment
import com.mangpo.bookclub.view.library.MyLibraryFragment
import com.mangpo.bookclub.view.library.SelectBookFragment
import com.mangpo.bookclub.view.write.RecordDetailFragment
import com.mangpo.bookclub.view.my_info.ChecklistManagementActivity
import com.mangpo.bookclub.view.my_info.GoalManagementActivity
import com.mangpo.bookclub.view.my_info.MyInfoActivity
import com.mangpo.bookclub.view.write.*
import com.mangpo.bookclub.viewmodel.BookViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var beforeMenu: Int = R.id.main

    private val bookVm: BookViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBook()  //책 데이터 불러오기

        //bottom navigation 메뉴 선택 시 프래그먼트 전환
        binding.bottomNavigation.setOnItemSelectedListener {
            val fm = supportFragmentManager
            val transaction = fm.beginTransaction()

            when (it.itemId) {
                R.id.main -> {
                    BackStackManager.pushBackstack(0)   //현재 눌러진 메뉴의 id 를 저장한다.

                    if (beforeMenu == it.itemId) {    //클릭 전 bottom menu 랑 클릭된 bottom menu 가 같을 때 -> WriteFrameFragment 를 보여준다.
                        Log.d("MainActivity", "bottomNavigation main 0")
                        BackStackManager.clearFragment(0)
                        val fragment = HomeFragment()
                        BackStackManager.pushFragment(0, fragment)
                        transaction.replace(binding.frameLayout.id, fragment)
                            .commitAllowingStateLoss()
                    } else {
                        var fragment = BackStackManager.peekFragment(0)  //최근 프래그먼트 가져오기

                        if (fragment == null) {   //클릭 전 bottom menu 랑 클릭된 bottom menu 가 다르고, 과거의 프래그먼트 스택 데이터가 없을 때 -> HomeFragment 를 보여준다.
                            Log.d("MainActivity", "bottomNavigation main 2")
                            fragment = HomeFragment()
                            BackStackManager.pushFragment(0, fragment)
                            transaction.replace(binding.frameLayout.id, fragment)
                                .commitAllowingStateLoss()
                        } else {    //클릭 전 bottom menu 랑 클릭된 bottom menu 가 다르고, 과거의 프래그먼트 스택 데이터가 있을 때 -> 가장 최근의 프래그먼트를 보여준다.
                            Log.d("MainActivity", "bottomNavigation main 3")
                            transaction.replace(binding.frameLayout.id, fragment)
                                .commitAllowingStateLoss()
                        }
                    }
                }
                R.id.library -> {
                    BackStackManager.pushBackstack(1)

                    if (beforeMenu == it.itemId) {    //클릭 전 bottom menu 랑 클릭된 bottom menu 가 같을 때 -> MyLibraryFragment 를 보여준다.
                        BackStackManager.clearFragment(1)
                        val fragment = MyLibraryFragment()
                        BackStackManager.pushFragment(1, fragment)
                        transaction.replace(binding.frameLayout.id, fragment)
                            .commitAllowingStateLoss()
                    } else {
                        var fragment = BackStackManager.peekFragment(1)  //최근 프래그먼트 가져오기

                        if (fragment == null) {   //클릭 전 bottom menu 랑 클릭된 bottom menu 가 다르고, 과거의 프래그먼트 스택 데이터가 없을 때 -> MyLibraryFragment 를 보여준다.
                            fragment = MyLibraryFragment()
                            BackStackManager.pushFragment(1, fragment)
                            transaction.replace(binding.frameLayout.id, fragment)
                                .commitAllowingStateLoss()
                        } else {    //클릭 전 bottom menu 랑 클릭된 bottom menu 가 다르고, 과거의 프래그먼트 스택 데이터가 있을 때 -> 가장 최근의 프래그먼트를 보여준다.
                            transaction.replace(binding.frameLayout.id, fragment)
                                .commitAllowingStateLoss()
                        }
                    }
                }
                R.id.bookclub -> {
                    Toast.makeText(this@MainActivity, "개발 중인 기능입니다.", Toast.LENGTH_SHORT).show()
                }
            }

            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            beforeMenu = it.itemId

            return@setOnItemSelectedListener true
        }

        binding.bottomNavigation.selectedItemId = beforeMenu
    }

    override fun onResume() {
        super.onResume()
        Log.d("MainActivity", "onResume")

    }

    override fun onBackPressed() {
        var currentFragment: Fragment? = null
        var nextFragment: Fragment? = null

        //현재 프래그먼트 구하기
        currentFragment = when (binding.bottomNavigation.selectedItemId) {
            R.id.main -> BackStackManager.peekFragment(0)
            R.id.library -> BackStackManager.peekFragment(1)
            else -> BackStackManager.peekFragment(2)
        }

        //현재 프래그먼트가 RecordFragment 이면 OnBackPressedListener 인터페이스의 onBackPressed 메서드 실행
        if (currentFragment is RecordFragment) {
            (currentFragment as OnBackPressedListener).onBackPressed()
            return
        }

        //이전 프래그먼트 구하기
        when (binding.bottomNavigation.selectedItemId) {
            R.id.main -> {
                BackStackManager.popFragment(0)
                nextFragment = BackStackManager.peekFragment(0)
            }

            R.id.library -> {
                BackStackManager.popFragment(1)
                nextFragment = BackStackManager.peekFragment(1)
            }

            R.id.bookclub -> {

            }
        }

        if (nextFragment == null)   //이전 프래그먼트가 없으면 이전 bottom menu 가 뭐였는지 확인하는 함수 실행
            popBackPressed()
        else {  //이전 프래그먼트가 있으면 그 프래그먼트로 이동
            supportFragmentManager.beginTransaction()
                .replace(binding.frameLayout.id, nextFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commitAllowingStateLoss()
        }
    }

    //mainBackStack 에 저장돼 있는 맨 뒤의 bottom navigation menu id 를 호출하는 함수
    private fun popBackPressed() {
        val menuIdx = BackStackManager.popBackstack()
        Log.d("MainActivity", "onBackPressed -> $menuIdx")

        //저장돼 있는 menu idx 가 없으면 앱을 종료하고 만약에 있으면 bottom navigation menu id 를 변경한다.
        if (menuIdx == null)
            finishAffinity()
        else {
            when (menuIdx) {
                0 -> binding.bottomNavigation.selectedItemId = R.id.main
                1 -> binding.bottomNavigation.selectedItemId = R.id.library
                2 -> binding.bottomNavigation.selectedItemId = R.id.bookclub
            }
        }
    }

    private fun initBook() {
        CoroutineScope(Dispatchers.Main).launch {
            bookVm.requestBookList("NOW")
            bookVm.requestBookList("AFTER")
            bookVm.requestBookList("BEFORE")
        }
    }

    //올라와 있는 키보드를 내리는 함수
    fun hideKeyBord(v: View) {
        val imm: InputMethodManager =
            getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }

    fun moveToRecord(isUpdate: Boolean) {
        changeFragment(RecordFragment(isUpdate), true)
    }

    fun moveToSelect() {
        changeFragment(SelectBookFragment(), true)
    }

    fun moveToWritingSetting(isUpdate: Boolean, delImgList: ArrayList<String>?) {
        if (delImgList == null)
            changeFragment(RecordSettingFragment(isUpdate), true)
        else {
            val fragment = RecordSettingFragment(isUpdate).apply {
                arguments = Bundle().apply {
                    putStringArrayList("delImgList", delImgList)
                }
            }
            changeFragment(fragment, true)
        }
    }

    fun moveToBookDesc(book: BookModel) {
        val fragment = BookDetailFragment().apply {
            arguments = Bundle().apply {
                putString("book", Gson().toJson(book))
            }
        }
        changeFragment(fragment, true)
    }

    fun moveToPostDetail(post: PostDetailModel) {
        Log.d("MainActivity", "moveToPostDetail $post")
        val fragment = RecordDetailFragment().apply {
            arguments = Bundle().apply {
                putString("post", Gson().toJson(post))
            }
        }
        changeFragment(fragment, true)
    }

    fun moveToMyLibrary() {
        changeFragment(MyLibraryFragment(), true)
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

    //현재 선택돼 있는 bottom menu 구하기
    fun getMenuIdx(): Int {
        return when (binding.bottomNavigation.selectedItemId) {
            R.id.main -> 0
            R.id.library -> 1
            else -> 2
        }
    }

    //store 이 true 이면 bottom menu 별로 backstack 에 저장 후 프래그먼트로 이동하고, false 이면 바로 이동한다.
    fun changeFragment(fragment: Fragment, store: Boolean) {
        if (store) {
            when (binding.bottomNavigation.selectedItemId) {
                R.id.main -> BackStackManager.pushFragment(0, fragment)
                R.id.library -> BackStackManager.pushFragment(1, fragment)
                R.id.bookclub -> BackStackManager.pushFragment(2, fragment)
            }
        }

        supportFragmentManager.beginTransaction().replace(binding.frameLayout.id, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commitAllowingStateLoss()
    }
}