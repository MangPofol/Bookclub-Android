package com.mangpo.bookclub.repository

import android.util.Log
import com.mangpo.bookclub.model.CheckListModel
import com.mangpo.bookclub.model.ChecklistsModel
import com.mangpo.bookclub.service.ChecklistService

class ChecklistRepository(private val service: ChecklistService) {

    suspend fun getChecklists(): ChecklistsModel? {
        val result = service.getChecklists()
        var checklists: ChecklistsModel? = null

        if (result.isSuccessful) {
            if (result.code() == 200) {
                checklists = result.body()
            } else {
                Log.e(
                    "ChecklistRepository", "getChecklists is not Successful!\n" +
                            "code: ${result.code()}\n" +
                            "message ${result.message()}"
                )
            }
        } else {
            Log.e(
                "ChecklistRepository", "getChecklists Error!\n" +
                        "code: ${result.code()}\n" +
                        "message ${result.message()}"
            )
        }

        return checklists
    }

    suspend fun createChecklist(checklist: CheckListModel): CheckListModel? {
        val result = service.createChecklist(checklist)
        var resBody: CheckListModel? = null

        if (result.isSuccessful) {
            when (result.code()) {
                201 -> {
                    Log.d("ChecklistRepository", "createChecklist Created! -> ${result.body()}")
                    resBody = result.body()!!
                }
                else -> {
                    Log.e(
                        "ChecklistRepository", "createChecklist is Not Successful!\n" +
                                "code: ${result.code()}\n" +
                                "body: $checklist"
                    )
                }
            }
        } else {
            Log.e(
                "ChecklistRepository", "createChecklist ERROR!\n" +
                        "code: ${result.code()}\n" +
                        "body: $checklist"
            )
        }

        return resBody
    }

    suspend fun updateChecklist(toDoId: Long, checklist: CheckListModel): Int {
        val result = service.updateChecklist(toDoId, checklist)

        if (result.isSuccessful) {
            when (result.code()) {
                204 -> {
                    Log.d("ChecklistRepository", "updateChecklist Update!")
                }
                else -> {
                    Log.e(
                        "ChecklistRepository", "createChecklist is Not Successful!\n" +
                                "code: ${result.code()}\n" +
                                "body: $checklist"
                    )
                }
            }

            return result.code()
        } else {
            Log.e(
                "ChecklistRepository", "updateChecklist ERROR!\n" +
                        "code: ${result.code()}\n" +
                        "body: $checklist"
            )

            return -1
        }
    }
}