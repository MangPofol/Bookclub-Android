package com.mangpo.ourpage.view.adpater

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.mangpo.ourpage.R
import com.mangpo.ourpage.databinding.ItemChecklistHeaderBinding
import com.mangpo.ourpage.model.remote.TodoGroupByMonth
import com.mangpo.ourpage.utils.fadeIn
import com.mangpo.ourpage.utils.fadeOut

class ChecklistHeaderRVAdapter() :
    RecyclerView.Adapter<ChecklistHeaderRVAdapter.ChecklistViewHolder>() {
    interface MyClickListener {
        fun delete(toDoId: Int)
    }

    private var checklists: List<TodoGroupByMonth> = arrayListOf()

    private lateinit var binding: ItemChecklistHeaderBinding
    private lateinit var myClickListener: MyClickListener

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChecklistHeaderRVAdapter.ChecklistViewHolder {
        binding =
            ItemChecklistHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ChecklistViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ChecklistHeaderRVAdapter.ChecklistViewHolder,
        position: Int
    ) {
        holder.headerTv.text = checklists[position].groupingDate
        holder.checklistCntTv.text = checklists[position].checklists.size.toString()
        holder.downIv.setOnClickListener {
            if (holder.bodyRv.visibility == View.GONE) {
                holder.headerTv.setBackgroundResource(R.drawable.bg_grey_top_8_null_null)
                holder.bodyRv.visibility = View.VISIBLE
                holder.bodyLayout.fadeIn()
            } else {
                holder.headerTv.setBackgroundResource(R.drawable.bg_grey_8_null_null)
                holder.bodyRv.visibility = View.GONE
                holder.bodyLayout.fadeOut()
            }
        }

        val checklistBodyRVAdapter: ChecklistBodyRVAdapter = ChecklistBodyRVAdapter()
        checklistBodyRVAdapter.setChecklists(checklists[position].checklists)
        checklistBodyRVAdapter.setMyClickListener(object : ChecklistBodyRVAdapter.MyClickListener {
            override fun delete(todoId: Int) {
                myClickListener.delete(todoId)
            }
        })
        holder.bodyRv.adapter = checklistBodyRVAdapter
        //내부 체크리스트의 스크롤이 안되는 현상을 해결하기 위한 코드
        holder.bodyRv.setOnTouchListener { v, event ->
            holder.bodyLayout.requestDisallowInterceptTouchEvent(true)

            return@setOnTouchListener false
        }
    }

    override fun getItemCount(): Int = checklists.size

    fun setChecklist(checklists: List<TodoGroupByMonth>) {
        this.checklists = checklists
        notifyDataSetChanged()
    }

    fun setMyClickListener(myClickListener: MyClickListener) {
        this.myClickListener = myClickListener
    }

    inner class ChecklistViewHolder(view: ItemChecklistHeaderBinding) :
        RecyclerView.ViewHolder(view.root) {
        val headerTv: TextView = view.checklistHeaderTv
        val checklistCntTv: TextView = view.checklistHeaderCntTv
        val downIv: ImageView = view.checklistHeaderDownIv
        val bodyLayout: ConstraintLayout = view.checklistBodyLayout
        val bodyRv: RecyclerView = view.checklistBodyRv
    }
}