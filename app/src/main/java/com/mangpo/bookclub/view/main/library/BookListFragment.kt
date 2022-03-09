package com.mangpo.bookclub.view.main.library

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentBookListBinding
import com.mangpo.bookclub.model.remote.Book
import com.mangpo.bookclub.utils.LogUtil
import com.mangpo.bookclub.utils.convertDpToPx
import com.mangpo.bookclub.view.BaseFragment
import com.mangpo.bookclub.view.adpater.BookRVAdapter
import com.mangpo.bookclub.viewmodel.BookViewModel

class BookListFragment(private val category: String) : BaseFragment<FragmentBookListBinding>(FragmentBookListBinding::inflate), TextWatcher {
    private val bookVm: BookViewModel by activityViewModels<BookViewModel>()

    private lateinit var bookRVAdapter: BookRVAdapter

    override fun initAfterBinding() {
        initAdapter()
        setMyEventListener()
        observe()
    }

    override fun onResume() {
        super.onResume()
        bookVm.getBooksByCategory(category)
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        if (p0!=null && p0.isNotEmpty()) {
            val books = ((bookVm.books.value) as ArrayList<Book>).filter { it.name.contains(p0) }
            bookRVAdapter.setData(books)
        } else
            setInitBook()
    }

    override fun afterTextChanged(p0: Editable?) {
    }

    private fun initAdapter() {
        bookRVAdapter = BookRVAdapter()
        bookRVAdapter.setMyClickListener(object : BookRVAdapter.MyClickListener {
            override fun sendBook(book: Book) {
                val action = LibraryFragmentDirections.actionLibraryFragmentToBookDetailFragment(Gson().toJson(book))
                findNavController().navigate(action)
            }
        })
        binding.bookListRv.adapter = bookRVAdapter
    }

    private fun setMyEventListener() {
        binding.bookListSearchCb.setOnCheckedChangeListener { compoundButton, b ->
            hideKeyboard()
            binding.bookListSearchEt.setText("")
            setInitBook()

            if (b) {
                binding.bookListSearchIv.setImageResource(R.drawable.ic_reading_glasses_white)
                binding.bookListSortCb.isChecked = false
                binding.bookListSearchEt.visibility = View.VISIBLE
                setRVMargin(71)
            } else {
                binding.bookListSearchIv.setImageResource(R.drawable.ic_reading_glasses_primary)
                binding.bookListSearchEt.visibility = View.GONE
                setRVMargin(41)
            }
        }

        binding.bookListSortCb.setOnCheckedChangeListener { compoundButton, b ->
            binding.bookListNewestCb.isChecked = false
            binding.bookListOldestCb.isChecked = false
            binding.bookListByNameCb.isChecked = false
            setInitBook()

            if (b) {
                binding.bookListSearchCb.isChecked = false
                binding.bookListNewestCb.visibility = View.VISIBLE
                binding.bookListOldestCb.visibility = View.VISIBLE
                binding.bookListByNameCb.visibility = View.VISIBLE
                setRVMargin(71)
            } else {
                binding.bookListNewestCb.visibility = View.GONE
                binding.bookListOldestCb.visibility = View.GONE
                binding.bookListByNameCb.visibility = View.GONE
                setRVMargin(41)
            }
        }

        binding.bookListSearchEt.addTextChangedListener(this)

        binding.bookListNewestCb.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                binding.bookListOldestCb.isChecked = false
                binding.bookListByNameCb.isChecked = false
            }

            setBookBySort()
        }

        binding.bookListOldestCb.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                binding.bookListNewestCb.isChecked = false
                binding.bookListByNameCb.isChecked = false
            }

            setBookBySort()
        }

        binding.bookListByNameCb.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                binding.bookListNewestCb.isChecked = false
                binding.bookListOldestCb.isChecked = false
            }

            setBookBySort()
        }
    }

    private fun setRVMargin(margin: Int) {
        val horizontalMargin = convertDpToPx(requireContext(), 10)
        val topMargin = convertDpToPx(requireContext(), margin)

        binding.bookListRv.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            setMargins(horizontalMargin, topMargin, horizontalMargin, 0)
        }
    }

    private fun setInitBook() {
        bookRVAdapter.setData(bookVm.books.value!!)
    }

    private fun setBookBySort() {
        val books = bookVm.books.value as ArrayList<Book>

        when {
            binding.bookListNewestCb.isChecked -> bookRVAdapter.setData(books.sortedWith(compareBy { it.modifiedDate }).reversed())
            binding.bookListOldestCb.isChecked -> bookRVAdapter.setData(books.sortedWith(compareBy { it.modifiedDate }))
            binding.bookListByNameCb.isChecked -> bookRVAdapter.setData(books.sortedWith(compareBy { it.name }))
            else -> setInitBook()
        }
    }

    private fun observe() {
        bookVm.nowBooks.observe(viewLifecycleOwner, Observer {
            LogUtil.d("BookListFragment", "nowBooks Observe! nowBooks -> $it")

            if (category=="NOW") {
                binding.bookListBookCntTv.text = it.size.toString()
                bookRVAdapter.setData(it)
            }
        })

        bookVm.afterBooks.observe(viewLifecycleOwner, Observer {
            LogUtil.d("BookListFragment", "afterBooks Observe! afterBooks -> $it")

            if (category=="AFTER") {
                binding.bookListBookCntTv.text = it.size.toString()
                bookRVAdapter.setData(it)
            }
        })

        bookVm.beforeBooks.observe(viewLifecycleOwner, Observer {
            LogUtil.d("BookListFragment", "beforeBooks Observe! beforeBooks -> $it")

            if (category=="BEFORE") {
                binding.bookListBookCntTv.text = it.size.toString()
                bookRVAdapter.setData(it)
            }
        })
    }
}