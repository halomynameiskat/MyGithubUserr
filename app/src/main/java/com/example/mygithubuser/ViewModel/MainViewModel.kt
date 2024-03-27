package com.example.mygithubuser.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mygithubuser.Data.Repository

import com.example.mygithubuser.Data.remote.response.DetailResponse

class MainViewModel(private val repository: Repository) : ViewModel() {

    private val _data = MutableLiveData<List<DetailResponse>>()
    val data: LiveData<List<DetailResponse>> = _data

    companion object {
        private const val TAG = "MainViewModel"
    }

    fun searchUser(githubName: String) = repository.findUser(githubName)
}