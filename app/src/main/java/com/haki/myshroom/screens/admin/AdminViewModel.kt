package com.haki.myshroom.screens.admin

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.haki.myshroom.classifier.Recognition
import com.haki.myshroom.data.AuthRepository
import com.haki.myshroom.data.MushroomRepository
import com.haki.myshroom.database.entity.UserEntity
import com.haki.myshroom.states.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {
    val userSession = authRepository.userSession.asLiveData()
    val users = mutableStateOf<List<UserEntity>>(emptyList())

    private val _updateResult: MutableStateFlow<ResultState<UserEntity>> =
        MutableStateFlow(ResultState.Loading)
    val updateResult: StateFlow<ResultState<UserEntity>>
        get() = _updateResult

    suspend fun fetchUsers(){
        users.value = authRepository.fetchUsers()
    }

    suspend fun updateUser(email: String, name: String, isPremium: Boolean, expDate: String) {
        val user = authRepository.updateUser(email, name, isPremium, expDate)
        Log.e("wasssss", user.toString())
        _updateResult.emit(user)
    }

    suspend fun logout() {
        authRepository.logout()
    }
}