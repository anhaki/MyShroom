package com.haki.myshroom.database.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "alarm")
data class MushroomEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val label: String,
    val timeHour: Int,
    val timeMin: Int,
    val isActive: Boolean = true,
    val isDaily: Boolean = false,
    val days: List<Int> = emptyList(),
    val currentMillis: Long = 0L,
    val volume: Float = 0.7F,
) : Parcelable