package com.etsproject.utils

import com.etsproject.data.Profile

interface ClickCallback {
    fun onItemClick(profile: Profile)
}