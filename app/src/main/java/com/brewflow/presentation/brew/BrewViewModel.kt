package com.brewflow.presentation.brew

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class BrewViewModel @Inject constructor(): ViewModel() {

    private val _currentStepIndex = MutableStateFlow(0)
    val currentStepIndex: StateFlow<Int> = _currentStepIndex

    private val _timerSecondsLeft = MutableStateFlow(0L)
    val timerSecondsLeft: StateFlow<Long> = _timerSecondsLeft

    private var countDownTimer: CountDownTimer? = null

    fun nextStep(totalSteps: Int) {
        _currentStepIndex.value = (_currentStepIndex.value + 1).coerceAtMost(totalSteps - 1)
    }

    fun prevStep() {
        _currentStepIndex.value = (_currentStepIndex.value - 1).coerceAtLeast(0)
    }

    fun startTimer(seconds: Int, onFinished: () -> Unit) {
        countDownTimer?.cancel()
        _timerSecondsLeft.value = seconds.toLong()
        countDownTimer = object : CountDownTimer(seconds * 1000L, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                _timerSecondsLeft.value = millisUntilFinished / 1000
            }

            override fun onFinish() {
                _timerSecondsLeft.value = 0
                onFinished()
            }
        }.start()
    }

    fun cancelTimer() {
        countDownTimer?.cancel()
        _timerSecondsLeft.value = 0
    }

    override fun onCleared() {
        super.onCleared()
        countDownTimer?.cancel()
    }
}
