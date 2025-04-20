package com.haki.myshroom.screens.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.haki.myshroom.data.AuthRepository
import com.haki.myshroom.states.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {
    val authState: LiveData<AuthState> = authRepository.authState.asLiveData()
    val userSession = authRepository.userSession.asLiveData()

    fun login(email: String, password: String) {
        authRepository.login(email, password)
    }

    fun register(email: String, name: String, password: String, confirmPassword: String) {
        authRepository.register(email, name, password, confirmPassword)
    }

    fun resetState() {
        authRepository.resetState()
    }

    suspend fun logout() {
        authRepository.logout()
    }
}