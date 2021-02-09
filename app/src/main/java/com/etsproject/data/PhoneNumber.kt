package com.etsproject.data

import java.io.Serializable

data class PhoneNumber(
    val countryCode: String,
    val number: String
) : Serializable {
    override fun toString(): String {
        return "$countryCode $number"
    }
}