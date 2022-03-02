package com.mangpo.ourpage.view.adpater

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mangpo.ourpage.databinding.ItemChecklistBodyBinding
import com.mangpo.ourpage.model.remote.Todo

class ChecklistBodyRVAdapter() :
    RecyclerView.Adapter<ChecklistBodyRVAdapter.ChecklistBodyHolder>() {
    interface MyClickListener {
        fun delete(todoId: Int)
    }

    private var checklists: ArrayList<Todo> = arrayListOf()

    private lateinit var binding: ItemChecklistBodyBinding
    private lateinit var myClickListener: MyClickListener

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
        holder.deleteIv.setOnClickListener {
            myClickListener.delete(checklists[position].toDoId)
        }
    }

    override fun getItemCount(): Int = checklists.size

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun setChecklists(checklists: ArrayList<Todo>) {
        this.checklists = checklists
        notifyDataSetChanged()
    }

    fun setMyClickListener(myClickListener: MyClickListener) {
        this.myClickListener = myClickListener
    }

    inner class ChecklistBodyHolder(itemView: ItemChecklistBodyBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val content: TextView = binding.checklistBodyTv
        val deleteIv: ImageView = binding.checklistBodyDeleteIv
    }
}