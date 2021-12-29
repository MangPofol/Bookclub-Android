package com.mangpo.bookclub.view.my_info

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.mangpo.bookclub.databinding.ActivityChecklistManagementBinding
import com.mangpo.bookclub.view.adapter.ChecklistHeaderRVAdapter
import com.mangpo.bookclub.viewmodel.ChecklistViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChecklistManagementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChecklistManagementBinding
    private lateinit var checklistHeaderAdapter: ChecklistHeaderRVAdapter

    private val checklistVm: ChecklistViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("ChecklistManagementActivity", "onCreate")
        binding = ActivityChecklistManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observe()
        getChecklists()
        initAdapter()

        binding.backIvView.setOnClickListener {
            finish()
        }
    }

    private fun getChecklists() {
        CoroutineScope(Dispatchers.Main).launch {
            checklistVm.getCompletedChecklists()
        }
    }

    private fun observe() {
        checklistVm.completedChecklists.observe(this, Observer {
            Log.d("ChecklistManagementActivity", "Observe!!\n${it.size}")

            if (it.size==0) {
                binding.checklistRv.visibility = View.GONE
                binding.noChecklistTv.visibility = View.VISIBLE
            } else {
                binding.checklistRv.visibility = View.VISIBLE
                binding.noChecklistTv.visibility = View.GONE
                checklistHeaderAdapter.setChecklist(it)
            }
        })
    }

    private fun initAdapter() {
        checklistHeaderAdapter = ChecklistHeaderRVAdapter()
        checklistHeaderAdapter.setChecklistRVAdapterListener(object :
            ChecklistHeaderRVAdapter.ChecklistRVAdapterListener {
            override fun onArrowClick() {

            }

        })
        binding.checklistRv.adapter = checklistHeaderAdapter
    }
}