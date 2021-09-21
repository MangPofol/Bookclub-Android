package com.example.bookclub.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookclub.model.ClubModel
import com.example.bookclub.repository.ClubRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ClubViewModel: ViewModel() {
    private val clubRepository: ClubRepository = ClubRepository()
    private val _clubs: MutableLiveData<MutableList<ClubModel>> = MutableLiveData<MutableList<ClubModel>>()

    private var tempClubs: MutableList<ClubModel> = ArrayList<ClubModel>()

    val clubs: LiveData<MutableList<ClubModel>> get() = _clubs

    suspend fun createClub(newClub: ClubModel): Int {
        val res = withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
            clubRepository.createClub(newClub)
        }

        if (res.code()==201) {
            Log.e("클럽 생성", res.body().toString())
            addClub(newClub)
        } else if (res.code()==400) {
            Log.e("클럽 생성 실패", res.toString())
        }

        return res.code()
    }

    suspend fun getClubsByUser() {
        viewModelScope.launch(Dispatchers.IO) {
            _clubs.postValue(clubRepository.getClubsByUser().body()!!.clubs)
        }
    }

    private fun addClub(newClub: ClubModel) {
        if (clubs.value!=null) {
            tempClubs = _clubs.value!!
        }
        tempClubs.add(newClub)
        _clubs.postValue(tempClubs)
    }
}