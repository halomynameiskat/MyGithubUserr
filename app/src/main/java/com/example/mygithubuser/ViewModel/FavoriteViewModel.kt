package com.example.mygithubuser.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mygithubuser.Data.Repository
import com.example.mygithubuser.Data.remote.response.DetailResponse

class FavoriteViewModel(private val repository: Repository) : ViewModel() {
    fun getAllChanges(): LiveData<List<DetailResponse>> = repository.getAllFavorite()

}
