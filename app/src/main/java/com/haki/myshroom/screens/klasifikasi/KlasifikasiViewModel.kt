package com.haki.myshroom.screens.klasifikasi

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.haki.myshroom.classifier.Classifier
import com.haki.myshroom.classifier.Recognition
import com.haki.myshroom.data.AuthRepository
import com.haki.myshroom.data.MushroomRepository
import com.haki.myshroom.states.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class KlasifikasiViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    mushroomRepository: MushroomRepository,
    private val application: Application,
) : ViewModel() {
    private var classifier: Classifier? = null
    val userSession = authRepository.userSession.asLiveData()
    val isPremiumState = authRepository.isPremiumState.asLiveData()

    private val _result: MutableStateFlow<ResultState<Recognition>> =
        MutableStateFlow(ResultState.Loading)
    val result: StateFlow<ResultState<Recognition>>
        get() = _result

    init {
        initModel()
    }

    fun resetResultState() {
        viewModelScope.launch {
            _result.emit(ResultState.Loading)
        }
    }

    private fun initModel() {
        if (classifier == null) {
            viewModelScope.launch {
                try {
                    classifier = Classifier(application)
                    Log.v("MarcosLog", "Classifier initialized")
                } catch (e: IOException) {
                    Log.e("MarcosLog", "init(): Failed to create Classifier", e)
                }
            }
        }
    }

    fun classify(bitmap: Bitmap, start: Long) {
        viewModelScope.launch {
            _result.emit(ResultState.Loading)
            try {
                val scaled = bitmap.scaleBitmap(224, 224)
                val recognition = classifier?.classify(scaled, start)
                if (recognition != null) {
                    _result.emit(ResultState.Success(recognition))
                } else {
                    _result.emit(ResultState.Error("Tidak terdapat jamur pada gambar"))
                }
            } catch (e: Exception) {
                _result.emit(ResultState.Error("Error during classification: ${e.message}"))
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        classifier?.close()
    }
}

fun Bitmap.scaleBitmap(width: Int, height: Int): Bitmap {
    return Bitmap.createScaledBitmap(this, width, height, false)
}
