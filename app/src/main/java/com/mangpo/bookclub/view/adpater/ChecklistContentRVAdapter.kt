package com.mangpo.bookclub.view.adpater

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.ItemChecklistContentBinding
import com.mangpo.bookclub.model.remote.Todo

class ChecklistContentRVAdapter() :
    RecyclerView.Adapter<ChecklistContentRVAdapter.ChecklistHolder>() {
    private lateinit var binding: ItemChecklistContentBinding
    private lateinit var myChecklistContentRVAdapterListener: MyChecklistContentRVAdapterListener

    private var todos: ArrayList<Todo> = arrayListOf()

    interface MyChecklistContentRVAdapterListener {
        fun addChecklist()   //체크리스트 추가
        fun updateChecklist(position: Int, checklist: Todo)   //체크리스트 수정
        fun completeChecklist(position: Int, checklist: Todo)  //체크리스트 완료
        fun deleteChecklist(position: Int, checklistId: Int)    //체크리스트 삭제
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChecklistContentRVAdapter.ChecklistHolder {
        binding = ItemChecklistContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ChecklistHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ChecklistContentRVAdapter.ChecklistHolder,
        position: Int
    ) {
        if (todos.isNotEmpty() && position<todos.size) {   //체크리스트가 있을 때 -> 체크리스트를 보여준다.
            holder.checkIv.setImageResource(R.drawable.ic_squrae)
            holder.checklistTv.visibility = View.VISIBLE
            holder.checklistTv.text = todos[position].content
            holder.checklistEmptyTv.visibility = View.INVISIBLE
            holder.deleteIv.visibility = View.VISIBLE
        } else if (todos.isEmpty() || (todos.isNotEmpty() && position>=todos.size)) {    //체크리스트가 없을 때 -> "눌러서 작성하세요" 체크리스트를 보여준다.
            holder.checkIv.setImageResource(R.drawable.ic_plus_secondary)
            holder.checklistTv.visibility = View.INVISIBLE
            holder.checklistEmptyTv.visibility = View.VISIBLE
            holder.deleteIv.visibility = View.INVISIBLE
        }

        holder.checklistEmptyTv.setOnClickListener { //체크리스트가 비어 있는 텍스트뷰 클릭 리스너 -> 체크리스트 추가
            myChecklistContentRVAdapterListener.addChecklist()
        }

        holder.checklistTv.setOnClickListener { //체크리스트가 있는 텍스트뷰 클릭 리스너 -> 체크리스트 수정
            myChecklistContentRVAdapterListener.updateChecklist(position, todos[position])
        }

        holder.checkIv.setOnClickListener {
            if (holder.checklistTv.visibility==View.VISIBLE)    //체크리스트 완료
                myChecklistContentRVAdapterListener.completeChecklist(position, todos[position])
            else    //체크리스트 추가
                myChecklistContentRVAdapterListener.addChecklist()
        }

        holder.deleteIv.setOnClickListener {    //삭제 이미지뷰 클릭 리스너 -> 체크리스트 삭제
            myChecklistContentRVAdapterListener.deleteChecklist(position, todos[position].toDoId!!)
        }
    }

    override fun getItemCount(): Int = 5

    private fun bindMainVer(holder: ChecklistContentRVAdapter.ChecklistHolder, position: Int) {
        if (todos.isNotEmpty() && position<todos.size) {   //체크리스트가 있을 때 -> 체크리스트를 보여준다.
            holder.checkIv.setImageResource(R.drawable.ic_squrae)
            holder.checklistTv.visibility = View.VISIBLE
            holder.checklistTv.text = todos[position].content
            holder.checklistEmptyTv.visibility = View.INVISIBLE
            holder.deleteIv.visibility = View.VISIBLE
        } else if (todos.isEmpty() || (todos.isNotEmpty() && position>=todos.size)) {    //체크리스트가 없을 때 -> "눌러서 작성하세요" 체크리스트를 보여준다.
            holder.checkIv.setImageResource(R.drawable.ic_plus_secondary)
            holder.checklistTv.visibility = View.INVISIBLE
            holder.checklistEmptyTv.visibility = View.VISIBLE
            holder.deleteIv.visibility = View.INVISIBLE
        }

        holder.checklistEmptyTv.setOnClickListener { //체크리스트가 비어 있는 텍스트뷰 클릭 리스너 -> 체크리스트 추가
            myChecklistContentRVAdapterListener.addChecklist()
        }

        holder.checklistTv.setOnClickListener { //체크리스트가 있는 텍스트뷰 클릭 리스너 -> 체크리스트 수정
            myChecklistContentRVAdapterListener.updateChecklist(position, todos[position])
        }

        holder.checkIv.setOnClickListener { //체크리스트 완료
            myChecklistContentRVAdapterListener.completeChecklist(position, todos[position])
        }

        holder.deleteIv.setOnClickListener {    //삭제 이미지뷰 클릭 리스너 -> 체크리스트 삭제
            myChecklistContentRVAdapterListener.deleteChecklist(position, todos[position].toDoId!!)
        }
    }

    fun  setTodos(todos: ArrayList<Todo>) {
        this.todos = todos

        notifyDataSetChanged()
    }

    //이벤트 리스너 인터페이스 설정 함수
    fun setMyChecklistContentRVAdapterListener(myChecklistContentRVAdapterListener: MyChecklistContentRVAdapterListener) {
        this.myChecklistContentRVAdapterListener = myChecklistContentRVAdapterListener
    }

    inner class ChecklistHolder(itemView: ItemChecklistContentBinding) : RecyclerView.ViewHolder(itemView.root) {
        val checklistTv: TextView = itemView.checklistContentTv
        val checklistEmptyTv: TextView = itemView.checklistContentEmptyTv
        val checkIv: ImageView = itemView.checklistContentIconIv
        val deleteIv: ImageView = itemView.checklistContentDeleteIv
    }

}