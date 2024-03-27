package com.example.mygithubuser.Data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mygithubuser.Data.remote.response.DetailResponse

@Database(entities = [DetailResponse::class], exportSchema = false, version = 1)
abstract class FavDB : RoomDatabase() {

    abstract fun favDao(): FavDao

    companion object {
        @Volatile
        private var INSTANCE: FavDB? = null

        @JvmStatic
        fun getDatabase(context: Context): FavDB {
            if (INSTANCE == null) {
                synchronized(FavDB::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        FavDB::class.java, "favorite_database"
                    ).build()
                }
            }
            return INSTANCE as FavDB
        }
    }
}