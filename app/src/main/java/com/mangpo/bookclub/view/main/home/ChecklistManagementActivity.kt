package com.mangpo.bookclub.view.main.home

import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.ActivityChecklistManagementBinding
import com.mangpo.bookclub.utils.LogUtil
import com.mangpo.bookclub.utils.isNetworkAvailable
import com.mangpo.bookclub.view.BaseActivity
import com.mangpo.bookclub.view.adpater.ChecklistHeaderRVAdapter
import com.mangpo.bookclub.viewmodel.TodoViewModel

class ChecklistManagementActivity : BaseActivity<ActivityChecklistManagementBinding>(ActivityChecklistManagementBinding::inflate) {
    private val todoVm: TodoViewModel by viewModels<TodoViewModel>()

    private lateinit var checklistHeaderRVAdapter: ChecklistHeaderRVAdapter

    override fun initAfterBinding() {
        initAdapter()
        observe()

        todoVm.getTodos()

        binding.checklistManagementTb.setNavigationOnClickListener {
            finish()
        }
    }

    private fun observe() {
        todoVm.completeTodos.observe(this, Observer {
            LogUtil.d("ChecklistManagementActivity", "completeTodos Observe! completeTodos -> $it")

            if (it.isEmpty())
                binding.checklistManagementRv.visibility = View.GONE
            else {
                binding.checklistManagementRv.visibility = View.VISIBLE
                checklistHeaderRVAdapter.setChecklist(it)
            }
        })

        todoVm.deleteTodoCode.observe(this, Observer {
            val code = it.getContentIfNotHandled()
            LogUtil.d("ChecklistManagementActivity", "deleteTodoCode Observe! deleteTodoCode -> $code")

            if (code!=null) {
                when (code) {
                    204 -> todoVm.getTodos()
                    else -> showSnackBar(getString(R.string.error_api))
                }
            }
        })
    }

    private fun initAdapter() {
        checklistHeaderRVAdapter = ChecklistHeaderRVAdapter()
        checklistHeaderRVAdapter.setMyClickListener(object : ChecklistHeaderRVAdapter.MyClickListener {
            override fun delete(toDoId: Int) {
                if (isNetworkAvailable(applicationContext))
                    todoVm.deleteTodo(toDoId)
                else
                    showSnackBar(getString(R.string.error_check_network))
            }
        })
        binding.checklistManagementRv.adapter = checklistHeaderRVAdapter
    }
}