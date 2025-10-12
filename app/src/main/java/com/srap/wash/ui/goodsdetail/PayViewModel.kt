package com.srap.wash.ui.goodsdetail

import androidx.lifecycle.viewModelScope
import com.srap.wash.logic.model.CreateTradeResponse
import com.srap.wash.logic.model.PrePayResponse
import com.srap.wash.logic.repository.NetworkRepo
import com.srap.wash.logic.state.LoadingState
import com.srap.wash.ui.base.BaseViewModel
import com.srap.wash.utils.ToastUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PayViewModel @Inject constructor(
    networkRepo: NetworkRepo,
) : BaseViewModel(networkRepo) {
    private val TAG = javaClass.simpleName

    fun createTrade(
        promotions: List<Map<String, String>>,
        items: List<Map<String, String>>,
        onResult: (CreateTradeResponse.Data?) -> Unit
    ) {
        viewModelScope.launch {
            networkRepo.createTrade(promotions, items)
                .collect { state ->
                    when (state) {
                        is LoadingState.Error -> {
                            ToastUtil.show("[$TAG] ${state.errMsg}")
                        }
                        is LoadingState.Success -> {
                            onResult(state.response)
                        }
                    }
                }
        }
    }

    fun prePay(
        orderNo: String,
        payType: Int,
        onResult: (PrePayResponse.Data?) -> Unit
    ) {
        viewModelScope.launch {
            networkRepo.prePay(orderNo, payType)
                .collect { state ->
                    when (state) {
                        is LoadingState.Error -> {
                            ToastUtil.show("[$TAG] ${state.errMsg}")
                        }
                        is LoadingState.Success -> {
                            onResult(state.response)
                        }
                    }
                }
        }
    }
}