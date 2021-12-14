package com.mangpo.bookclub.view.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.ItemChecklistBinding

class ChecklistRVAdapter() : RecyclerView.Adapter<ChecklistRVAdapter.ChecklistHolder>() {
    private lateinit var binding: ItemChecklistBinding

    private val checklist: ArrayList<String> =
        arrayListOf("체크리스트1", "눌러서 작성하세요.", "눌러서 작성하세요.", "눌러서 작성하세요.", "눌러서 작성하세요.")

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChecklistRVAdapter.ChecklistHolder {
        binding = ItemChecklistBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ChecklistHolder(binding)
    }

    override fun onBindViewHolder(holder: ChecklistRVAdapter.ChecklistHolder, position: Int) {
        holder.checklistEt.addTextChangedListener(ChecklistTextWatcher(position))

        if (checklist[position]=="눌러서 작성하세요.") {
        } else {
            holder.checklistEt.setText(checklist[position])
        }

    }

    override fun getItemCount(): Int = checklist.size

    inner class ChecklistHolder(itemView: ItemChecklistBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val checklistEt: EditText = itemView.checklistEt
    }

    inner class ChecklistTextWatcher(var position: Int): TextWatcher {
        private var isCreate: Boolean = true

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            isCreate = s.toString()=="눌러서 작성하세요."
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {

        }

    }
}