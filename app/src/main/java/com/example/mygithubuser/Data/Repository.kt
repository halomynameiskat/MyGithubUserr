package com.example.mygithubuser.Data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.mygithubuser.Data.local.FavDao
import com.example.mygithubuser.Data.remote.api.ApiService
import com.example.mygithubuser.Data.remote.response.DetailResponse
import com.example.mygithubuser.Data.remote.response.FollowResponse
import kotlinx.coroutines.Dispatchers

class Repository(
    private val apiService: ApiService,
    private val mFavDao: FavDao,
) {
    fun findUser(githubName: String): LiveData<Resource<List<DetailResponse>>> = liveData {
        emit(Resource.Loading)
        try {
            val response = apiService.getGithub(githubName)
            emit(Resource.Success(response.items))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }

    fun detailUser(githubProfile: String): LiveData<Resource<DetailResponse>> =
        liveData(Dispatchers.IO) {
            emit(Resource.Loading)
            try {
                if (mFavDao.isFav(githubProfile)) {
                    emit(Resource.Success(mFavDao.getFav(githubProfile)))
                } else {
                    val response = apiService.getDetail(githubProfile)
                    emit(Resource.Success(response))
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.message.toString()))
            }
        }

    fun followerUser(githubFollower: String): LiveData<Resource<List<FollowResponse>>> = liveData {
        emit(Resource.Loading)
        try {
            val response = apiService.getFollower(githubFollower)
            emit(Resource.Success(response))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }

    fun followingUser(githubFollowing: String): LiveData<Resource<List<FollowResponse>>> =
        liveData {
            emit(Resource.Loading)
            try {
                val response = apiService.getFollowing(githubFollowing)
                emit(Resource.Success(response))
            } catch (e: Exception) {
                emit(Resource.Error(e.message.toString()))
            }
        }

    suspend fun insert(favorite: DetailResponse) {
        mFavDao.addFav(favorite)
    }

    fun getAllFavorite(): LiveData<List<DetailResponse>> = mFavDao.getAllFav()

    fun isFavorite(name: String): Boolean = mFavDao.isFav(name)

    suspend fun delete(favorite: DetailResponse) {
        mFavDao.deleteFav(favorite)
    }

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            apiService: ApiService,
            favoriteDao: FavDao,
        ): Repository = instance ?: synchronized(this) {
            instance ?: Repository(apiService, favoriteDao)
        }.also { instance = it }
    }
}