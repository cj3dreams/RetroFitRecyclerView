package com.pseudoencom.newsapp.data

import androidx.room.*

@Dao
interface ApiTokenDao {
    @Query("SELECT * FROM apiToken ORDER BY id DESC")
    fun getToken(): List<ApiTokenEntity>?

    @Insert
    fun insert(apiToken: ApiTokenEntity?)

    @Delete
    fun delete(apiToken: ApiTokenEntity?)

    @Update
    fun update(apiToken: ApiTokenEntity?)
}