package com.haki.myshroom.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.haki.myshroom.database.entity.MushroomEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlarm(alarm: MushroomEntity): Long

    @Query("SELECT * FROM alarm")
    fun getAllAlarm(): Flow<List<MushroomEntity>>

    @Query("SELECT * FROM alarm WHERE id LIKE :id")
    fun getAlarmById(id: Long): Flow<List<MushroomEntity>>

    @Query("DELETE FROM alarm")
    suspend fun deleteAll()

    @Query("DELETE FROM alarm WHERE id LIKE :id")
    suspend fun deleteAlarm(id: Long)

    @Query("DELETE FROM alarm WHERE isActive LIKE 0")
    suspend fun deleteAllAlarms()

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateAlarm(mushroomEntity: MushroomEntity)

    @Query("UPDATE alarm SET isActive = :isActive WHERE id = :id")
    suspend fun updateAlarmStatus(id: Long, isActive: Boolean)
}