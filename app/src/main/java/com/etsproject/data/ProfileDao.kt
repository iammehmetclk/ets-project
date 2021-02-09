package com.etsproject.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {

    @Query("SELECT * FROM profile_table WHERE name || surname LIKE '%' || :query || '%' ORDER BY name COLLATE NOCASE")
    fun getProfileList(query: String): Flow<List<Profile>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: Profile)

    @Update
    suspend fun updateProfile(profile: Profile)

    @Delete
    suspend fun deleteProfile(profile: Profile)
}