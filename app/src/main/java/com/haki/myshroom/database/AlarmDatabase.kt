package com.haki.myshroom.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.haki.myshroom.database.entity.Converters
import com.haki.myshroom.database.entity.MushroomEntity

@Database(
    entities = [MushroomEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AlarmDatabase : RoomDatabase() {
    abstract fun alarmDao(): AlarmDao
}