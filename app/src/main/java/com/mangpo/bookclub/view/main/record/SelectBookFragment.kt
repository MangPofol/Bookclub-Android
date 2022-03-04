package com.mangpo.bookclub.view.main.record

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentSelectBookBinding
import com.mangpo.bookclub.model.remote.Book
import com.mangpo.bookclub.utils.PrefsUtils
import com.mangpo.bookclub.utils.isNetworkAvailable
import com.mangpo.bookclub.view.BaseFragment
import com.mangpo.bookclub.view.adpater.BookRVAdapter
import com.mangpo.bookclub.view.bottomsheet.BookCategoryBottomSheetFragment
import com.mangpo.bookclub.viewmodel.BookViewModel

class SelectBookFragment : BaseFragment<FragmentSelectBookBinding>(FragmentSelectBookBinding::inflate), TextWatcher {
    private val bookVm: BookViewModel by viewModels<BookViewModel>()

    private lateinit var bookRVAdapter: BookRVAdapter
    private lateinit var bookCategoryBottomSheetFragment: BookCategoryBottomSheetFragment
    private lateinit var book: Book

    override fun initAfterBinding() {
        setMyEventListener()
        initAdapter()
        initDialog()
        observe()

        if (binding.selectBookEt.text.isBlank())
            bindBooksVerLibrary()
        else
            bindBooksVerKakao(binding.selectBookEt.text.toString())
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        if (p0!=null && p0.isNotEmpty())
            bindBooksVerKakao(p0.toString())
        else
            bindBooksVerLibrary()
    }

    override fun afterTextChanged(p0: Editable?) {
    }

    private fun setMyEventListener() {
        binding.selectBookTb.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.selectBookEt.addTextChangedListener(this)

        binding.selectBookRg.setOnCheckedChangeListener { radioGroup, i ->
            bindBooksVerLibrary()
        }
    }

    private fun initAdapter() {
        bookRVAdapter = BookRVAdapter()
        bookRVAdapter.setMyClickListener(object : BookRVAdapter.MyClickListener {
            override fun sendBook(book: Book) {
                this@SelectBookFragment.book = book

                if (book.id==null)
                    bookCategoryBottomSheetFragment.show(requireActivity().supportFragmentManager, null)
                else
                    goRecord()
            }
        })

        binding.selectBookRv.adapter = bookRVAdapter
    }

    private fun bindBooksVerLibrary() {
        binding.selectBookTv.visibility = View.VISIBLE
        binding.selectBookRg.visibility = View.VISIBLE

        binding.selectBookSearchResultTv.visibility = View.INVISIBLE
        binding.selectBookLineView.visibility = View.INVISIBLE

        bookRVAdapter.clearData()

        when (binding.selectBookRg.checkedRadioButtonId) {
            R.id.select_book_reading_rb -> bookVm.getBooksByCategory("NOW")
            R.id.select_book_reading_complete_rb -> bookVm.getBooksByCategory("AFTER")
            R.id.select_book_reading_before_rb -> bookVm.getBooksByCategory("BEFORE")
        }
    }

    private fun bindBooksVerKakao(title: String?) {
        binding.selectBookTv.visibility = View.INVISIBLE
        binding.selectBookRg.visibility = View.INVISIBLE

        binding.selectBookSearchResultTv.visibility = View.VISIBLE
        binding.selectBookLineView.visibility = View.VISIBLE

        if (title==null)
            bookRVAdapter.clearData()
        else
            bookVm.searchBooks(title, "target", 20)
    }

    private fun initDialog() {
        bookCategoryBottomSheetFragment = BookCategoryBottomSheetFragment()
        bookCategoryBottomSheetFragment.setMyDialogCallback(object : BookCategoryBottomSheetFragment.MyDialogCallback {
            override fun getCategory(category: String) {
                book.category = category

                if (category=="BEFORE") {
                    if (!isNetworkAvailable(requireContext()))
                        showNetworkSnackBar()
                    else
                        bookVm.createBook(book)
                } else
                    goRecord()
            }
        })
    }

    private fun goRecord() {
        val action = SelectBookFragmentDirections.actionSelectBookFragmentToRecordFragment("CREATE", PrefsUtils.getTempRecord(), Gson().toJson(book))
        findNavController().navigate(action)
    }

    private fun observe() {
        bookVm.kakaoBooks.observe(viewLifecycleOwner, Observer {
            Log.d("SelectBookFragment", "kakaoBooks Observe! kakaoBooks -> $it")
            bookRVAdapter.setData(it)
        })

        bookVm.books.observe(viewLifecycleOwner, Observer {
            Log.d("SelectBookFragment", "books Observe! books -> $it")

            if (binding.selectBookRg.visibility==View.VISIBLE)
                bookRVAdapter.setData(it)
        })

        bookVm.createBookCode.observe(viewLifecycleOwner, Observer {
            val code = it.getContentIfNotHandled()
            Log.d("SelectBookFragment", "createBookCode Observe! createBookCode -> $code")

            if (code!=null) {
                when (code) {
                    201 -> {
                        showToast(getString(R.string.msg_add_book_success))
                        book.id = bookVm.newBook!!.id
                        val action = SelectBookFragmentDirections.actionSelectBookFragmentToBookDetailFragment(Gson().toJson(book))
                        findNavController().navigate(action)
                    }
                    else -> showSnackBar(getString(R.string.error_api))
                }
            }
        })
    }
}