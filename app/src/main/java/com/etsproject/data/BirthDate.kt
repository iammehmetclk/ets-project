package com.etsproject.data

import java.io.Serializable

data class BirthDate(
    val day: Int,
    val month: Int,
    val year: Int
) : Serializable {
    override fun toString(): String {
        val dayText = if (day < 10) "0$day"
        else "$day"
        val monthText = if (month < 10) "0$month"
        else "$month"
        return "$dayText/$monthText/$year"
    }
}