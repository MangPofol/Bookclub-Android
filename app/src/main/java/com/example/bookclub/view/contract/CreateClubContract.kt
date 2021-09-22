package com.example.bookclub.view.contract

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContract
import com.example.bookclub.model.ClubModel
import com.example.bookclub.view.bookclub.CreateClubActivity

class CreateClubContract : ActivityResultContract<Any, ClubModel>() {
    override fun createIntent(context: Context, input: Any?): Intent {

        return Intent(context, CreateClubActivity::class.java)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): ClubModel? {
        return if (resultCode == Activity.RESULT_OK) {
            val bundle: Bundle = intent!!.getBundleExtra("newClub")!!
            val newClub: ClubModel = ClubModel(
                bundle.getLong("id"),
                bundle.getString("name")!!,
                bundle.getString("colorSet")!!,
                bundle.getInt("level"),
                bundle.getLong("presidentId"),
                bundle.getString("description")!!,
                bundle.getString("createdDate")!!,
                bundle.getString("modifiedDate")!!
            )
            Log.e("CreateClubContract", "클럽 생성 완료! -> ${newClub.toString()}")

            newClub
        } else {
            Log.e("CreateClubContract", "클럽 생성 실패!")

            null
        }
    }
}