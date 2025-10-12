package com.srap.wash.ui.order

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.srap.wash.logic.model.OrderListResponse
import com.srap.wash.logic.repository.NetworkRepo
import com.srap.wash.logic.state.LoadingState
import com.srap.wash.ui.base.BaseRefreshLoadViewModel
import com.srap.wash.utils.ToastUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    networkRepo: NetworkRepo,
) : BaseRefreshLoadViewModel<OrderListResponse.Item>(networkRepo) {
    private val TAG = javaClass.simpleName

    var data by mutableStateOf<OrderListResponse.Data?>(null)
        private set

    override fun fetchData() {
        viewModelScope.launch {
            val orderList = fetchOrderList()
            super.fetchData()
            if (orderList == null || orderList.items.isNullOrEmpty()) {
                isEmpty = true
                return@launch
            }
            isEmpty = false
            page = orderList.total
            list += orderList.items
            return@launch
        }
    }

    private suspend fun fetchOrderList(): OrderListResponse.Data? {
        if (isLoadMore) page += 1
        return when (val state = networkRepo.getOrderList(page).first()) {
            is LoadingState.Error -> {
                ToastUtil.show("[$TAG] ${state.errMsg}")
                null
            }
            is LoadingState.Success -> {
                data = state.response
                data
            }
        }
    }

}