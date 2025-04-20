package com.haki.myshroom.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.haki.myshroom.data.AuthRepository
import com.haki.myshroom.data.MushroomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    mushroomRepository: MushroomRepository,
) : ViewModel() {
    val userSession = authRepository.userSession.asLiveData()
    val isPremiumState = authRepository.isPremiumState.asLiveData()

    suspend fun logout() {
        authRepository.logout()
    }
}