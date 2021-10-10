package com.mangpo.bookclub.view.library

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentMyLibraryBinding
import com.mangpo.bookclub.model.BookModel
import com.mangpo.bookclub.util.HorizontalItemDecorator
import com.mangpo.bookclub.view.adapter.BookAdapter
import com.mangpo.bookclub.view.adapter.BookClubFilterAdapter
import com.mangpo.bookclub.view.adapter.MyLibraryPagerAdapter
import com.mangpo.bookclub.view.main.MainActivity
import com.mangpo.bookclub.viewmodel.BookViewModel
import com.mangpo.bookclub.viewmodel.ClubViewModel
import com.mangpo.bookclub.viewmodel.MyLibraryViewModel
import com.mangpo.bookclub.viewmodel.PostViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyLibraryFragment : Fragment(), TextWatcher {
    private lateinit var binding: FragmentMyLibraryBinding
    private lateinit var myLibraryPagerAdapter: MyLibraryPagerAdapter
    private lateinit var bookClubFilterAdapter: BookClubFilterAdapter

    private val myLibraryViewModel: MyLibraryViewModel by activityViewModels<MyLibraryViewModel>()
    private val bookViewModel: BookViewModel by activityViewModels<BookViewModel>()
    private val clubViewModel: ClubViewModel by activityViewModels<ClubViewModel>()
    private val postViewModel: PostViewModel by activityViewModels<PostViewModel>()

    private var books: MutableList<BookModel> = ArrayList<BookModel>()
    private var adapter: BookAdapter = BookAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("MyLibrary", "onCreate")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e("MyLibrary", "onCreateView")
        binding = FragmentMyLibraryBinding.inflate(inflater, container, false)  //뷰바인딩 초기화

        myLibraryPagerAdapter = MyLibraryPagerAdapter(context as FragmentActivity)
        binding.viewPager.adapter = myLibraryPagerAdapter  //어댑터 설정

        //페이지 변환 후 호출되는 콜백 함수
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.e("ViewPagerFragment", "Page ${position + 1}")
                myLibraryViewModel.updateLibraryReadType(position)

                //완독 부분에선 북클럽 필터 GONE
                when (position) {
                    0 -> setVisibilityClubButton(View.VISIBLE)
                    1 -> setVisibilityClubButton(View.VISIBLE)
                    2 -> setVisibilityClubButton(View.GONE)
                }
            }
        })

        //main 검색 버튼 클릭 리스너
        binding.searchButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.clubButton.isChecked = false
                binding.sortButton.isChecked = false
                binding.searchLayout.root.visibility = View.VISIBLE
                binding.searchLayout.searchBookET.addTextChangedListener(this)
                binding.searchLayout.searchBookET.text.clear()
            } else {
                binding.searchLayout.root.visibility = View.GONE
            }
        }

        //main 북클럽 필터 버튼 클릭 리스너
        binding.clubButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.searchButton.isChecked = false
                binding.sortButton.isChecked = false

                if (clubViewModel.clubs.value!!.size != 0) {
                    binding.bookClubFilterLayout.root.visibility = View.VISIBLE
                    bookClubFilterAdapter = BookClubFilterAdapter(clubViewModel.clubs.value!!)
                    binding.bookClubFilterLayout.clubFilterRecyclerView.adapter =
                        bookClubFilterAdapter
                    binding.bookClubFilterLayout.clubFilterRecyclerView.addItemDecoration(
                        HorizontalItemDecorator(10)
                    )
                }
            } else {
                binding.bookClubFilterLayout.root.visibility = View.GONE
            }
        }

        //main 정렬 필터 버튼 클릭 리스너
        binding.sortButton.setOnCheckedChangeListener { buttonView, isChecked ->
            //scrollUp()
            if (isChecked) {
                binding.searchButton.isChecked = false
                binding.clubButton.isChecked = false
                binding.sortFilterLayout.root.visibility = View.VISIBLE
                binding.sortFilterLayout.latestOrder.isChecked = false
                binding.sortFilterLayout.oldOrder.isChecked = false
                binding.sortFilterLayout.nameOrder.isChecked = false
            } else {
                binding.sortFilterLayout.root.visibility = View.GONE
            }
        }

        //읽는중, 완독, 읽고싶은 observe
        myLibraryViewModel.libraryReadType.observe(viewLifecycleOwner, Observer {
            Log.e("libraryReadType observe", it.toString())

            (requireActivity() as MainActivity).hideKeyBord(this.requireView()) //키보드가 올라와 있다면 내리기
            binding.searchLayout.root.visibility = View.GONE
            binding.bookClubFilterLayout.root.visibility = View.GONE
            binding.sortFilterLayout.root.visibility = View.GONE
            binding.searchButton.isChecked = false
            binding.clubButton.isChecked = false
            binding.sortButton.isChecked = false

            adapter = myLibraryPagerAdapter.getAdapter(it)

            when (it) {
                0 ->CoroutineScope(Dispatchers.Main).launch {
                        books = bookViewModel.getBooks("NOW")!!
                        adapter.setBooks(books)
                    }
                1 -> CoroutineScope(Dispatchers.Main).launch {
                    books = bookViewModel.getBooks("AFTER")!!
                    adapter.setBooks(books)
                }
                2 -> CoroutineScope(Dispatchers.Main).launch {
                    binding.viewPager.currentItem = 2
                    books = bookViewModel.getBooks("BEFORE")!!
                    adapter.setBooks(books)
                }
            }
        })

        //정렬-최신순 필터 체크 리스너
        binding.sortFilterLayout.latestOrder.setOnCheckedChangeListener { checkBox, isChecked ->
            if (isChecked) {
                binding.sortFilterLayout.oldOrder.isChecked = false
                binding.sortFilterLayout.nameOrder.isChecked = false
            } else {
                adapter.setBooks(books)
            }
        }

        //정렬-오래된순 필터 체크 리스너
        binding.sortFilterLayout.oldOrder.setOnCheckedChangeListener { checkBox, isChecked ->
            if (isChecked) {
                binding.sortFilterLayout.latestOrder.isChecked = false
                binding.sortFilterLayout.nameOrder.isChecked = false
            } else {
                adapter.setBooks(books)
            }
        }

        //정렬-이름순 필터 체크 리스너
        binding.sortFilterLayout.nameOrder.setOnCheckedChangeListener { checkBox, isChecked ->
            if (isChecked) {
                binding.sortFilterLayout.latestOrder.isChecked = false
                binding.sortFilterLayout.oldOrder.isChecked = false
                adapter.setBooks(books.sortedWith(compareBy { it.name }) as MutableList<BookModel>)
            } else {
                adapter.setBooks(books)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("MyLibrary", "onViewCreated")

        (activity as MainActivity).setDrawer(binding.toolbar)   //navigation drawer 등록
        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_more_vert_36_black)  //navigation icon 설정

        TabLayoutMediator(binding.readTypeTabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.reading)
                1 -> tab.text = getString(R.string.read_complete)
                2 -> tab.text = getString(R.string.want_to_read)
                else -> null
            }
        }.attach()
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        adapter.setBooks(books.filter {
            it.name!!.contains(s!!)
        } as MutableList<BookModel>)
    }

    override fun afterTextChanged(s: Editable?) {
    }

    private fun setVisibilityClubButton(visibility: Int) {
        binding.clubButton.visibility = visibility
    }

}

