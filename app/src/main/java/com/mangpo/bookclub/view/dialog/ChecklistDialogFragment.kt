package com.mangpo.bookclub.view.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentChecklistDialogBinding
import com.mangpo.bookclub.model.remote.Todo
import com.mangpo.bookclub.utils.DialogFragmentUtils
import com.mangpo.bookclub.utils.isNetworkAvailable
import com.mangpo.bookclub.viewmodel.TodoViewModel

class ChecklistDialogFragment : DialogFragment() {
    private val todoVm: TodoViewModel by activityViewModels<TodoViewModel>()
    private val args: ChecklistDialogFragmentArgs by navArgs()

    private lateinit var binding: FragmentChecklistDialogBinding
    private lateinit var todo: Todo

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChecklistDialogBinding.inflate(inflater, container, false)

        setMyEventListener()
        observe()

        //다이얼로그 프래그먼트 모서리 둥글게
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        //전체 프래그먼트 크기 설정
        DialogFragmentUtils.dialogFragmentResize(
            requireContext(),
            this@ChecklistDialogFragment,
            0.84f,
            0.2f
        )

        val todoStr = args.checklist
        if (todoStr!=null) {
            todo = Gson().fromJson(todoStr, Todo::class.java)
            binding.checklistDialogEditEt.setText(todo.content)
        }
    }

    private fun setMyEventListener() {
        binding.checklistDialogCloseIv.setOnClickListener {
            dismiss()
        }

        binding.checklistDialogCompleteTv.setOnClickListener {
            if (binding.checklistDialogEditEt.text.toString().isBlank())
                Toast.makeText(requireContext(), getString(R.string.msg_input_content), Toast.LENGTH_SHORT).show()
            else if (!isNetworkAvailable(requireContext()))
                Snackbar.make(requireView(), getString(R.string.error_check_network), Snackbar.LENGTH_SHORT)
            else if (::todo.isInitialized)
                todoVm.updateTodo(todo.toDoId, binding.checklistDialogEditEt.text.toString(), false)
            else
                todoVm.createTodo(binding.checklistDialogEditEt.text.toString())
        }
    }

    private fun observe() {
        todoVm.createTodoCode.observe(viewLifecycleOwner, Observer {
            val code = it.getContentIfNotHandled()

            if (code!=null) {
                when (code) {
                    204 -> {
                        todoVm.getTodos()
                        dismiss()
                    }
                    else -> Snackbar.make(requireView(), getString(R.string.error_api), Snackbar.LENGTH_SHORT)
                }
            }
        })

        todoVm.updateTodoCode.observe(viewLifecycleOwner, Observer {
            val code = it.getContentIfNotHandled()

            if (code!=null) {
                when (code) {
                    204 -> {
                        todoVm.getTodos()
                        dismiss()
                    }
                    else -> Snackbar.make(requireView(), getString(R.string.error_api), Snackbar.LENGTH_SHORT)
                }
            }
        })
    }
}