package com.haki.myshroom.database.entity

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromListToString(days: List<Int>?): String? {
        return days?.joinToString(separator = ",")
    }

    @TypeConverter
    fun fromStringToList(daysString: String?): List<Int> {
        return if (daysString.isNullOrEmpty()) {
            emptyList()
        } else {
            daysString.split(",").map { it.toInt() }
        }
    }
}
