package com.mangpo.bookclub.view.main

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayoutMediator
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.ActivityMainBinding
import com.mangpo.bookclub.repository.UserRepository
import com.mangpo.bookclub.view.adapter.BottomNavigationPagerAdapter
import com.mangpo.bookclub.view.adapter.NavigationViewViewpagerAdapter
import com.mangpo.bookclub.view.contract.CreateClubContract
import com.mangpo.bookclub.viewmodel.BookViewModel
import com.mangpo.bookclub.viewmodel.MyLibraryViewModel
import kotlinx.coroutines.*
import com.mangpo.bookclub.viewmodel.PostViewModel
import gun0912.tedimagepicker.builder.TedImagePicker
import gun0912.tedimagepicker.builder.type.MediaType

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mDrawerToggle: ActionBarDrawerToggle

    private var bottomNavigationPagerAdapter: BottomNavigationPagerAdapter =
        BottomNavigationPagerAdapter(supportFragmentManager)
    private var userRepository: UserRepository = UserRepository()

    private val bookViewModel: BookViewModel by viewModels<BookViewModel>()
    private val myLibraryViewModel: MyLibraryViewModel by viewModels<MyLibraryViewModel>()
    private val postViewModel: PostViewModel by viewModels<PostViewModel>()
    private val navigationViewTabText: ArrayList<String> = arrayListOf("알림", "나의 북클럽", "설정")
    private val navigationViewTabIcon: ArrayList<Int> = arrayListOf(R.drawable.alarm_icon, R.drawable.my_bookclub_icon, R.drawable.setting_icon)

    val galleryPermissionCallback = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions() // ◀ 퍼미션 요청 + ACCESS_FINE_LOCATION 지정
    ) { isGranted ->
        if (isGranted["android.permission.READ_EXTERNAL_STORAGE"] == true && isGranted["android.permission.WRITE_EXTERNAL_STORAGE"] == true && isGranted["android.permission.CAMERA"]==true) {
            //이미지 선택하는 화면으로 이동
            TedImagePicker.with(this)
                .mediaType(MediaType.IMAGE)
                .cameraTileBackground(R.color.grey1)
                .title(R.string.gallery_title)
                .backButton(R.drawable.back_icon)
                .max(4, R.string.max_image_desc)
                .buttonBackground(R.color.white)
                .buttonTextColor(R.color.main_blue)
                .dropDownAlbum()
                .startMultiImage {
                        uriList -> postViewModel.updateImgUriList(uriList as MutableList<Uri>)
                }
        }
        else
            Toast.makeText(baseContext, "갤러리 접근 권한이 없습니다.", Toast.LENGTH_SHORT).show()
    }

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

        CoroutineScope(Dispatchers.Main).launch {
            withContext(CoroutineScope(Dispatchers.Main).coroutineContext) {
                bookViewModel.getBooks("NOW")!!
                bookViewModel.getBooks("AFTER")!!
                bookViewModel.getBooks("BEFORE")!!
            }
        }

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
                    bookViewModel.clearSelectedBook()
                    postViewModel.updateImgUriList(null)

                    return@setOnItemSelectedListener true
                }
                R.id.myBookclub -> {
                    binding.bottomViewPager.currentItem = 2
                    bookViewModel.clearSelectedBook()
                    postViewModel.updateImgUriList(null)

                    return@setOnItemSelectedListener true
                }
            }

            return@setOnItemSelectedListener false
        }

        val navigationViewpagerAdapter: NavigationViewViewpagerAdapter = NavigationViewViewpagerAdapter(this)
        binding.navigationViewLayout.navigationViewVp.adapter = navigationViewpagerAdapter

        TabLayoutMediator(binding.navigationViewLayout.navigationViewTab, binding.navigationViewLayout.navigationViewVp) {
            tab, position ->
                tab.text = navigationViewTabText[position]
                tab.icon = getDrawable(navigationViewTabIcon[position])
        }.attach()
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
        val imm: InputMethodManager =
            getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }
}