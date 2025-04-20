package com.haki.myshroom.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.haki.myshroom.data.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(val authRepository: AuthRepository) : ViewModel() {
    val authState = authRepository.authState.asLiveData()
    val userSession = authRepository.userSession.asLiveData()

    suspend fun updatePremiumEveryOpen() {
        authRepository.updatePremiumEveryOpen()
    }
}