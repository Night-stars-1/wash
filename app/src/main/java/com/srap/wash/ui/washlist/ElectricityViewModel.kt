package com.srap.wash.ui.washlist

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.srap.wash.logic.model.ReserveResponse
import com.srap.wash.module.IElecBillNetworkRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ElectricityViewModel @Inject constructor(
    val networkRepo: IElecBillNetworkRepo?,
) : ViewModel() {

    var data by mutableStateOf<ReserveResponse?>(null)
        private set

    init {
        fetchWashList()
    }

    private fun fetchWashList() {
        viewModelScope.launch {
            networkRepo?.getReserve()
                ?.collect { result ->
                    data = result.getOrNull()
                    Log.i("TAG", "fetchWashList: ${result.isFailure} $data")
                }
        }
    }
}
