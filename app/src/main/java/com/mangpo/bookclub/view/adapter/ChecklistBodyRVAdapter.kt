package com.mangpo.bookclub.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mangpo.bookclub.databinding.ItemChecklistBodyBinding
import com.mangpo.bookclub.model.CheckListModel

class ChecklistBodyRVAdapter() :
    RecyclerView.Adapter<ChecklistBodyRVAdapter.ChecklistBodyHolder>() {

    private var checklists: ArrayList<CheckListModel> = arrayListOf()

    private lateinit var binding: ItemChecklistBodyBinding

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChecklistBodyRVAdapter.ChecklistBodyHolder {
        binding =
            ItemChecklistBodyBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ChecklistBodyHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ChecklistBodyRVAdapter.ChecklistBodyHolder,
        position: Int
    ) {
        holder.content.text = checklists[position].content
    }

    override fun getItemCount(): Int = checklists.size

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun setChecklists(checklists: ArrayList<CheckListModel>) {
        this.checklists = checklists
        notifyDataSetChanged()
    }

    inner class ChecklistBodyHolder(itemView: ItemChecklistBodyBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val content: TextView = binding.checklistTv
    }
}