package com.mangpo.bookclub.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.mangpo.bookclub.databinding.ItemChecklistContentBinding
import com.mangpo.bookclub.model.CheckListModel

class ChecklistContentRVAdapter() :
    RecyclerView.Adapter<ChecklistContentRVAdapter.ChecklistHolder>() {
    private lateinit var binding: ItemChecklistContentBinding
    private lateinit var myChecklistContentRVAdapterListener: MyChecklistContentRVAdapterListener

    private var checklist: ArrayList<CheckListModel> = arrayListOf()

    interface MyChecklistContentRVAdapterListener {
        fun addChecklist(position: Int, content: String)   //체크리스트 추가
        fun updateChecklist(position: Int, toDoId: Long, content: String)   //체크리스트 수정
        fun completeChecklist(position: Int, checklist: CheckListModel)  //체크리스트 완료
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChecklistContentRVAdapter.ChecklistHolder {
        binding =
            ItemChecklistContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ChecklistHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ChecklistContentRVAdapter.ChecklistHolder,
        position: Int
    ) {
        holder.checklistEt.setText(checklist[position].content)
        holder.checkIv.setOnClickListener {
            myChecklistContentRVAdapterListener.completeChecklist(position, checklist[position])
        }
    }

    override fun getItemCount(): Int = 5    //무조건 5개로 고정

    //이벤트 리스너 인터페이스 설정 함수
    fun setMyChecklistContentRVAdapterListener(myChecklistContentRVAdapterListener: MyChecklistContentRVAdapterListener) {
        this.myChecklistContentRVAdapterListener = myChecklistContentRVAdapterListener
    }

    fun setChecklist(checklist: ArrayList<CheckListModel>) {
        while (checklist.size < 5) {
            checklist.add(CheckListModel())
        }

        this.checklist = checklist
    }

    fun removeChecklist(position: Int) {
        checklist.removeAt(position)
        notifyItemRemoved(position)
        setChecklist(checklist)
    }

    inner class ChecklistHolder(itemView: ItemChecklistContentBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val checklistEt: EditText = itemView.checklistEt
        val checkIv: ImageView = itemView.checkIv
    }

}