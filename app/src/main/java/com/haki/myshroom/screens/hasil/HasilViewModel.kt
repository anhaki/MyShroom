package com.haki.myshroom.screens.hasil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.haki.myshroom.data.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HasilViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {
    val userSession = authRepository.userSession.asLiveData()
    val isPremiumState = authRepository.isPremiumState.asLiveData()
}