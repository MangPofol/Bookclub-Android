package com.mangpo.ourpage.view.main.library

import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.mangpo.ourpage.R
import com.mangpo.ourpage.databinding.FragmentLibraryBinding
import com.mangpo.ourpage.view.BaseFragment
import com.mangpo.ourpage.view.adpater.LibraryFragmentAdapter

class LibraryFragment : BaseFragment<FragmentLibraryBinding>(FragmentLibraryBinding::inflate) {
    private lateinit var libraryFragmentAdapter: LibraryFragmentAdapter

    override fun initAfterBinding() {
        setMyEventListener()
        initAdapter()
    }

    private fun setMyEventListener() {
        binding.librarySettingIv.setOnClickListener {
            findNavController().navigate(R.id.action_libraryFragment_to_settingActivity)
        }
    }

    private fun initAdapter() {
        libraryFragmentAdapter = LibraryFragmentAdapter(this)
        binding.libraryVp.adapter = libraryFragmentAdapter

        val tabTitles: List<String> = listOf(getString(R.string.title_reading), getString(R.string.title_reading_complete), getString(R.string.title_reading_before))
        TabLayoutMediator(binding.libraryTabLayout, binding.libraryVp) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }
}