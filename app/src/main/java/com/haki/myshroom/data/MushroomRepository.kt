package com.haki.myshroom.data

import com.haki.myshroom.database.AlarmDao
import com.haki.myshroom.preferences.UserPreference
import io.github.jan.supabase.SupabaseClient
import javax.inject.Inject

class MushroomRepository @Inject constructor(
    private val userPreference: UserPreference,
    private val sbClient: SupabaseClient,
    private val dbDao: AlarmDao,
)