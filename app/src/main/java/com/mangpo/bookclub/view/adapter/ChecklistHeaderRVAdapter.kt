package com.mangpo.bookclub.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.ItemChecklistHeaderBinding
import com.mangpo.bookclub.model.ChecklistGroupByMonthModel

class ChecklistHeaderRVAdapter() :
    RecyclerView.Adapter<ChecklistHeaderRVAdapter.ChecklistViewHolder>() {

    private var checklists: List<ChecklistGroupByMonthModel> = arrayListOf()

    private lateinit var binding: ItemChecklistHeaderBinding
    private lateinit var checklistRVAdapterListener: ChecklistRVAdapterListener

    interface ChecklistRVAdapterListener {
        fun onArrowClick()
    }

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
        holder.dateTv.text = checklists[position].groupingDate
        holder.checklistCntTv.text = checklists[position].checklists.size.toString()
        holder.downIv.setOnClickListener {
            if (holder.bodyRv.visibility == View.GONE) {
                holder.headerView.setBackgroundResource(R.drawable.write_init_fragment_grey13_top_round_view)
                holder.bodyLayout.visibility = View.VISIBLE
                holder.bodyRv.visibility = View.VISIBLE
            } else {
                holder.headerView.setBackgroundResource(R.drawable.grey13_round_view)
                holder.bodyLayout.visibility = View.GONE
                holder.bodyRv.visibility = View.GONE
            }
        }

        val checklistBodyRVAdapter: ChecklistBodyRVAdapter = ChecklistBodyRVAdapter()
        checklistBodyRVAdapter.setChecklists(checklists[position].checklists)
        holder.bodyRv.adapter = checklistBodyRVAdapter
        //내부 체크리스트의 스크롤이 안되는 현상을 해결하기 위한 코드
        holder.bodyRv.setOnTouchListener { v, event ->
            holder.bodyLayout.requestDisallowInterceptTouchEvent(true)

            return@setOnTouchListener false
        }
    }

    override fun getItemCount(): Int = checklists.size

    fun setChecklistRVAdapterListener(checklistRVAdapterListener: ChecklistRVAdapterListener) {
        this.checklistRVAdapterListener = checklistRVAdapterListener
    }

    fun setChecklist(checklists: List<ChecklistGroupByMonthModel>) {
        this.checklists = checklists
        notifyDataSetChanged()
    }

    inner class ChecklistViewHolder(view: ItemChecklistHeaderBinding) :
        RecyclerView.ViewHolder(view.root) {
        val headerView: View = view.checklistHeaderView
        val dateTv: TextView = view.dateTv
        val checklistCntTv: TextView = view.checklistCntTv
        val downIv: ImageView = view.downIv
        val bodyLayout: ConstraintLayout = view.bodyLayout
        val bodyRv: RecyclerView = view.checklistContentRv
    }
}