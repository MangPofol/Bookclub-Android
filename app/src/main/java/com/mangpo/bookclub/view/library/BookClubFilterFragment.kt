package com.mangpo.bookclub.view.library

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.mangpo.bookclub.databinding.FragmentBookClubFilterBinding
import com.mangpo.bookclub.model.ClubModel
import com.mangpo.bookclub.util.HorizontalItemDecorator
import com.mangpo.bookclub.view.adapter.BookClubFilterAdapter
import com.mangpo.bookclub.viewmodel.ClubViewModel
import kotlinx.coroutines.*

class BookClubFilterFragment : Fragment() {
    private lateinit var binding: FragmentBookClubFilterBinding
    private lateinit var bookClubFilterAdapter: BookClubFilterAdapter

    private val clubViewModel: ClubViewModel by activityViewModels<ClubViewModel>()

    private var clubs: List<ClubModel> = ArrayList<ClubModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("BookClubFilterFragment", "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e("BookClubFilterFragment", "onCreateView")
        binding = FragmentBookClubFilterBinding.inflate(inflater, container, false)

        CoroutineScope(Dispatchers.Main).launch {
            clubViewModel.getClubsByUser()

            if (clubViewModel.clubs.value!!.size == 0) {
                binding.clubFilterRecyclerView.removeAllViewsInLayout()
            } else {
                Log.e("BookClubFilterFragment", "clubs ${clubViewModel.clubs.value!!}")
                bookClubFilterAdapter = BookClubFilterAdapter(clubViewModel.clubs.value!!)
                binding.clubFilterRecyclerView.adapter = bookClubFilterAdapter
            }
        }

        binding.clubFilterRecyclerView.addItemDecoration(HorizontalItemDecorator(10))

        return binding.root
    }

    override fun onDetach() {
        super.onDetach()
        Log.e("BookClubFilterFragment", "onDetach")
    }
}