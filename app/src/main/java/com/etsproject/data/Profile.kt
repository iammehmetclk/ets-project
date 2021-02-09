package com.etsproject.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "profile_table")
data class Profile(
    @PrimaryKey
    val pk: Long,
    val name: String?,
    val surname: String?,
    val birthDate: BirthDate?,
    val mailAddress: String?,
    val phoneNumber: PhoneNumber?,
    val note: String?
) : Serializable