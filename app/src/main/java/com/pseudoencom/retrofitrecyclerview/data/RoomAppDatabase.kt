package com.pseudoencom.retrofitrecyclerview.data

import ArticlesDao
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ArticlesEntity::class], version = 1)
abstract class RoomAppDatabase: RoomDatabase() {

    abstract fun articlesDao(): ArticlesDao?

    companion object{
        private var INSTANCE: RoomAppDatabase? = null

        fun getAppDatabase(context: Context): RoomAppDatabase?{
            if (INSTANCE == null){
                INSTANCE = Room.databaseBuilder<RoomAppDatabase>(
                    context.applicationContext, RoomAppDatabase::class.java, "AppDB")
                    .allowMainThreadQueries().build()
            }
            return INSTANCE
        }
    }
}