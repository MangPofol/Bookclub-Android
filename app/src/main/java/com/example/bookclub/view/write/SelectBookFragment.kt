package com.example.bookclub.view.write

import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.bookclub.R
import com.example.bookclub.databinding.FragmentRecordBinding
import com.example.bookclub.databinding.FragmentSelectBookBinding
import com.example.bookclub.viewmodel.SelectBookViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SelectBookFragment : Fragment(), TextWatcher {
    private lateinit var binding: FragmentSelectBookBinding
    private lateinit var selectBookViewModel: SelectBookViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selectBookViewModel = ViewModelProvider(this)[SelectBookViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectBookBinding.inflate(inflater, container, false)

        selectBookViewModel.books.observe(viewLifecycleOwner, Observer {
            Log.e("observe!!", it.toString())
        })

        //책 검색 EditText에 TextChanged 리스너 등록
        binding.searchBookET.addTextChangedListener(this)

        binding.readTypeRG.setOnCheckedChangeListener { group, checkedId ->

        }

        return binding.root
    }

    override fun onPause() {
        super.onPause()
        (parentFragment as WriteFragment).changeChildFragment(0)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        GlobalScope.launch {
            selectBookViewModel.updateSearchBookTitle(s.toString())
        }

        if (count != 0) {
            binding.selectBookTV.visibility = View.INVISIBLE
            binding.readTypeRG.visibility = View.INVISIBLE
        } else {
            binding.selectBookTV.visibility = View.VISIBLE
            binding.readTypeRG.visibility = View.VISIBLE

        }
    }

    override fun afterTextChanged(s: Editable?) {

    }
}