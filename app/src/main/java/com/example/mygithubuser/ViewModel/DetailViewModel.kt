package com.example.mygithubuser.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygithubuser.Data.Repository
import com.example.mygithubuser.Data.remote.response.DetailResponse
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: Repository) : ViewModel() {

    fun isFavorite(name: String): Boolean = repository.isFavorite(name)

    fun getDetail(githubProfile: String) = repository.detailUser(githubProfile)

    fun setFavUser(user: DetailResponse) = viewModelScope.launch {
        repository.insert(user)
    }

    fun deleteFavUser(user: DetailResponse) = viewModelScope.launch {
        repository.delete(user)
    }
}