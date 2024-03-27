package com.example.mygithubuser.Activity

import android.content.Context
import com.example.mygithubuser.Data.Repository
import com.example.mygithubuser.Data.local.FavDB
import com.example.mygithubuser.Data.remote.api.ApiConfig

object Injection {
    fun provideRepository(context: Context): Repository {
        val apiService = ApiConfig.getApiService()
        val database = FavDB.getDatabase(context)
        val dao = database.favDao()
        return Repository.getInstance(apiService, dao)
    }
}