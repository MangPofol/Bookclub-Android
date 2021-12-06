package com.mangpo.bookclub.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.ItemChecklistBinding

class ChecklistRVAdapter() : RecyclerView.Adapter<ChecklistRVAdapter.ChecklistHolder>() {
    private lateinit var binding: ItemChecklistBinding

    private val checklist: ArrayList<String> =
        arrayListOf("눌러서 작성하세요.", "눌러서 작성하세요.", "눌러서 작성하세요.", "눌러서 작성하세요.", "눌러서 작성하세요.")

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChecklistRVAdapter.ChecklistHolder {
        binding = ItemChecklistBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ChecklistHolder(binding)
    }

    override fun onBindViewHolder(holder: ChecklistRVAdapter.ChecklistHolder, position: Int) {
        holder.checklistTv.text = checklist[position]
    }

    override fun getItemCount(): Int = checklist.size

    inner class ChecklistHolder(itemView: ItemChecklistBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val checklistTv: TextView = itemView.checklistTv
    }
}