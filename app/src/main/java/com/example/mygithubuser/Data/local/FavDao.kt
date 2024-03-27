package com.example.mygithubuser.Data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mygithubuser.Data.remote.response.DetailResponse

@Dao
interface FavDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFav(favorite: DetailResponse)

    @Query("SELECT * FROM DetailResponse")
    fun getAllFav(): LiveData<List<DetailResponse>>

    @Query("SELECT * FROM DetailResponse WHERE DetailResponse.login LIKE :name")
    fun searchFav(name: String): LiveData<List<DetailResponse>>

    @Query("SELECT * FROM DetailResponse WHERE DetailResponse.login = :name")
    fun getFav(name: String): DetailResponse

    @Query("SELECT EXISTS(SELECT * FROM DetailResponse WHERE DetailResponse.login = :name)")
    fun isFav(name: String): Boolean

    @Delete
    suspend fun deleteFav(favorite: DetailResponse)
}