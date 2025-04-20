package com.haki.myshroom.di

import android.content.Context
import androidx.room.Room
import com.haki.myshroom.BuildConfig
import com.haki.myshroom.preferences.UserPreference
import com.haki.myshroom.preferences.dataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient {
        return createSupabaseClient(
            supabaseKey = BuildConfig.SUPABASE_KEY,
            supabaseUrl = BuildConfig.SUPABASE_URL,
        ) {
            install(Auth)
            install(Postgrest)
            install(Realtime)
        }
    }

    @Provides
    @Singleton
    fun provideUserPreference(@ApplicationContext context: Context): UserPreference {
        return UserPreference(context.dataStore)
    }
}
