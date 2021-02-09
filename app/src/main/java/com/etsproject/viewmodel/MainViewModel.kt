package com.etsproject.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.etsproject.data.Profile
import com.etsproject.data.ProfileRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: ProfileRepository) : ViewModel() {

    init {
        setQuery("")
    }

    val profileList: LiveData<List<Profile>> = repository.profileFlow.asLiveData()

    fun setQuery(query: String) {
        repository.query.value = query
    }

    fun insertProfile(profile: Profile) = viewModelScope.launch {
        repository.insertProfile(profile)
    }

    fun updateProfile(profile: Profile) = viewModelScope.launch {
        repository.updateProfile(profile)
    }

    fun deleteProfile(profile: Profile) = viewModelScope.launch {
        repository.deleteProfile(profile)
    }
}