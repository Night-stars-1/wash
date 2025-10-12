package com.srap.wash.ui.base

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.srap.wash.logic.repository.NetworkRepo

abstract class BaseViewModel(
    open val networkRepo: NetworkRepo,
) : ViewModel() {
    var isLoaded by mutableStateOf(false)
}