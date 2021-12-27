package com.mangpo.bookclub.view.book_profile

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentExpressMeBinding

class ExpressMeFragment : Fragment(), TextWatcher {

    private lateinit var binding: FragmentExpressMeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExpressMeBinding.inflate(inflater, container, false)

        (activity as BookProfileInitActivity).visibleSkipTV()
        (activity as BookProfileInitActivity).unEnableNextBtn()

        binding.expressMeEt.addTextChangedListener(this)

        return binding.root
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (count==0)
            (activity as BookProfileInitActivity).unEnableNextBtn()
        else
            (activity as BookProfileInitActivity).enableNextBtn()
    }

    override fun afterTextChanged(s: Editable?) {
    }

    fun getExpressText(): String = binding.expressMeEt.text.toString()
}