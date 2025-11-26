package com.brewflow.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brewflow.data.model.BrewingMethodDto
import com.brewflow.domain.usecase.GetMethodsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMethods: GetMethodsUseCase
) : ViewModel() {

    private val _methods = MutableStateFlow<List<BrewingMethodDto>>(emptyList())
    val methods: StateFlow<List<BrewingMethodDto>> = _methods

    private val _loading = MutableStateFlow(true)
    val loading: StateFlow<Boolean> = _loading

    init {
        viewModelScope.launch {
            try {
                _loading.value = true
                _methods.value = getMethods()
            } catch (e: Exception) {
                _methods.value = emptyList()
            } finally {
                _loading.value = false
            }
        }
    }

    fun suggestMethodForFlavor(flavor: String): BrewingMethodDto? {
        return _methods.value.firstOrNull { it.flavorProfiles.contains(flavor) }
    }
}
