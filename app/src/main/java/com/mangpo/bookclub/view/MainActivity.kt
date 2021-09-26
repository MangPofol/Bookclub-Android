package com.mangpo.bookclub.view

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.viewpager.widget.ViewPager
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.ActivityMainBinding
import com.mangpo.bookclub.repository.UserRepository
import com.mangpo.bookclub.view.adapter.BottomNavigationPagerAdapter
import com.mangpo.bookclub.view.contract.CreateClubContract
import com.mangpo.bookclub.viewmodel.BookViewModel
import com.mangpo.bookclub.viewmodel.MyLibraryViewModel
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mDrawerToggle: ActionBarDrawerToggle

    private var bottomNavigationPagerAdapter: BottomNavigationPagerAdapter =
        BottomNavigationPagerAdapter(supportFragmentManager)
    private var userRepository: UserRepository = UserRepository()

    private val bookViewModel: BookViewModel by viewModels<BookViewModel>()
    private val myLibraryViewModel: MyLibraryViewModel by viewModels<MyLibraryViewModel>()

    init {
        GlobalScope.launch {
            val user: HashMap<String, String> = HashMap()
            user["email"] = ""
            user["password"] = ""
            userRepository.login(user)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //북클럽 생성 activity 로 이동하는 contract 선언
        val launcher = registerForActivityResult(CreateClubContract()) {
            if (it != null) { //정상적으로 북클럽이 생성됐다면
                binding.bottomViewPager.currentItem = 2 //북클럽 fragment 로 이동한다.
                bottomNavigationPagerAdapter.sendNewClub(it)
            }
        }

        //navigation drawer 화면 메뉴 아이템 클릭 리스너
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            // Handle menu item selected
            when (menuItem.itemId) {
                R.id.myBookclub -> {
                    binding.drawerLayout.closeDrawers() //navigation drawer 닫기
                    launcher.launch(null)   //CreateClubContract launch(MainActivity -> CreateClubActivity)
                }
            }

            true
        }

        CoroutineScope(Dispatchers.Main).launch {
            withContext(CoroutineScope(Dispatchers.Main).coroutineContext) {
                bookViewModel.getBooks("NOW")!!
                bookViewModel.getBooks("AFTER")!!
                bookViewModel.getBooks("BEFORE")!!
            }
        }


        //뷰페이저 어댑터 설정&페이지 변환 리스너 설정
        binding.bottomViewPager.adapter = bottomNavigationPagerAdapter
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

        binding.bottomViewPager.currentItem = 1
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

    fun goToBeforeLibrary() {
        binding.bottomViewPager.currentItem = 1
        myLibraryViewModel.updateLibraryReadType(2)
    }

    //올라와 있는 키보드를 내리는 함수
    fun hideKeyBord(v: View) {
        val imm: InputMethodManager = getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }

    override fun onBackPressed() {
        /*//view pager의 현재 프래그먼트 가져오기
        var fragment: Fragment = binding.bottomViewPager.adapter!!.instantiateItem(
            binding.bottomViewPager,
            binding.bottomViewPager.currentItem
        ) as Fragment*/

        //drawer layout이 열려 있는 상태면 -> drawer layout 부터 닫는다.
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START))
            binding.drawerLayout.closeDrawers()
        else {
            super.onBackPressed()
        }

    }
}