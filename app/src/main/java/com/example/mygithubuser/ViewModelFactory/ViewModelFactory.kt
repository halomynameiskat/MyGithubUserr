package com.example.mygithubuser.ViewModelFactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mygithubuser.Activity.Injection
import com.example.mygithubuser.Data.Repository
import com.example.mygithubuser.ViewModel.DetailViewModel
import com.example.mygithubuser.ViewModel.FavoriteViewModel
import com.example.mygithubuser.ViewModel.FollowViewModel
import com.example.mygithubuser.ViewModel.MainViewModel

class ViewModelFactory constructor(private val repository: Repository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> return MainViewModel(
                repository
            ) as T
            modelClass.isAssignableFrom(FavoriteViewModel::class.java) -> return FavoriteViewModel(
                repository
            ) as T
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> return DetailViewModel(
                repository
            ) as T
            modelClass.isAssignableFrom(FollowViewModel::class.java) -> return FollowViewModel(
                repository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class: " + modelClass.name)
    }

    companion object {
        @Volatile
        internal var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory = instance ?: synchronized(this) {
            instance ?: ViewModelFactory(Injection.provideRepository(context))
        }.also { instance = it }
    }
}