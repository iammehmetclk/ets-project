package com.etsproject.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest

class ProfileRepository(private val profileDao: ProfileDao) {

    val query = MutableStateFlow("")
    val profileFlow = query.flatMapLatest {
        profileDao.getProfileList(it)
    }

    suspend fun insertProfile(profile: Profile) {
        profileDao.insertProfile(profile)
    }

    suspend fun updateProfile(profile: Profile) {
        profileDao.updateProfile(profile)
    }

    suspend fun deleteProfile(profile: Profile) {
        profileDao.deleteProfile(profile)
    }

    companion object {
        @Volatile
        private var instance: ProfileRepository? = null
        fun getInstance(profileDao: ProfileDao): ProfileRepository {
            instance?.let { return it }
            synchronized(this) {
                val temp = ProfileRepository(profileDao)
                instance = temp
                return temp
            }
        }
    }
}