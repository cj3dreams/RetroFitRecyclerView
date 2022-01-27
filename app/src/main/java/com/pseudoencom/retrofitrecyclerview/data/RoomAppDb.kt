package com.pseudoencom.retrofitrecyclerview.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ArticlesEntity::class], version = 1, exportSchema = false)
abstract class RoomAppDb: RoomDatabase() {

    abstract fun articlesDao(): ArticlesDao?

    companion object{
        private var INSTANCE: RoomAppDb? = null

        fun getAppDatabase(context: Context): RoomAppDb? {
            if (INSTANCE == null){
                INSTANCE = Room.databaseBuilder<RoomAppDb>(context.applicationContext, RoomAppDb::class.java, "AppDB")
                    .allowMainThreadQueries()
                    .build()
            }
            return INSTANCE
        }
    }
}