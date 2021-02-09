package com.etsproject.data

import androidx.room.TypeConverter
import com.google.gson.Gson

class Converters {

    @TypeConverter
    fun birthDateToJson(birthDate: BirthDate): String = Gson().toJson(birthDate)

    @TypeConverter
    fun jsonToBirthDate(json: String): BirthDate = Gson().fromJson(json, BirthDate::class.java)

    @TypeConverter
    fun phoneNumberToJson(phoneNumber: PhoneNumber): String = Gson().toJson(phoneNumber)

    @TypeConverter
    fun jsonToPhoneNumber(json: String): PhoneNumber = Gson().fromJson(json, PhoneNumber::class.java)
}