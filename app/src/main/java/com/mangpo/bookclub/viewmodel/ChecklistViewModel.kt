package com.mangpo.bookclub.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mangpo.bookclub.model.CheckListModel
import com.mangpo.bookclub.model.ChecklistGroupByMonthModel
import com.mangpo.bookclub.repository.ChecklistRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class ChecklistViewModel(private val repository: ChecklistRepository) : ViewModel() {
    //완료되지 않은 체크리스트를 저장하는 변수
    val unCompletedChecklists: MutableLiveData<ArrayList<CheckListModel>> = MutableLiveData()

    //완료된 체크리스트를 저장하는 변수
    val completedChecklists: MutableLiveData<ArrayList<ChecklistGroupByMonthModel>> =
        MutableLiveData()

    suspend fun getUnCompletedChecklists() {
        viewModelScope.launch {
            val checklists = repository.getChecklists()?.data as ArrayList<CheckListModel>

            unCompletedChecklists.value = checklists.filter {
                !it.isComplete
            } as ArrayList<CheckListModel>
        }
    }

    suspend fun getCompletedChecklists() {
        viewModelScope.launch {
            var checklists = repository.getChecklists()?.data as ArrayList<CheckListModel>

            checklists = checklists.filter {
                it.isComplete
            } as ArrayList<CheckListModel>

            completedChecklists.value = groupByDate(checklists)
        }
    }

    suspend fun createChecklist(checklist: CheckListModel) {
        viewModelScope.launch {
            val checklist = repository.createChecklist(checklist)

            if (checklist != null) {
                getUnCompletedChecklists()
            }

        }
    }

    suspend fun updateChecklist(position: Int, checklist: CheckListModel) {
        viewModelScope.launch {
            val code = repository.updateChecklist(checklist.toDoId!!, checklist)

            if (code == 204 && !checklist.isComplete) {
                unCompletedChecklists.value!![position] = checklist
                unCompletedChecklists.postValue(unCompletedChecklists.value)
            } else {
                getUnCompletedChecklists()
            }
        }
    }

    //월별로 완료된 체크리스트 그룹화하기
    private fun groupByDate(checklist: List<CheckListModel>): ArrayList<ChecklistGroupByMonthModel> {
        val checklistGroupByMonthModel: ArrayList<ChecklistGroupByMonthModel> = arrayListOf()

        checklist.forEach { it ->
            val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val output = SimpleDateFormat("yyyy.MM")
            val dateStr = output.format(input.parse(it.createDate))

            val element = checklistGroupByMonthModel.find { it.groupingDate == dateStr }
            if (element == null) {
                checklistGroupByMonthModel.add(ChecklistGroupByMonthModel(dateStr, arrayListOf(it)))
            } else {
                val idx = checklistGroupByMonthModel.indexOf(element)
                checklistGroupByMonthModel[idx].checklists.add(it)
            }
        }

        return checklistGroupByMonthModel
    }

}