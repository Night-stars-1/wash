package com.srap.wash.ui.scancode

import androidx.lifecycle.viewModelScope
import com.srap.wash.logic.model.GoodsScanResponse
import com.srap.wash.logic.repository.NetworkRepo
import com.srap.wash.logic.state.Code
import com.srap.wash.logic.state.LoadingState
import com.srap.wash.ui.base.BaseViewModel
import com.srap.wash.utils.ToastUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "ScanCodeViewModel"

@HiltViewModel
class ScanCodeViewModel @Inject constructor(
    networkRepo: NetworkRepo,
) : BaseViewModel(networkRepo) {

    fun goodsScan(nqt: String, onError: () -> Unit, onResult: (goodsId: String) -> Unit) {
        viewModelScope.launch {
            networkRepo.goodsScan(nqt = nqt)
                .collect{ state ->
                    when (state) {
                        is LoadingState.Error -> {
                            ToastUtil.show("[$TAG] ${state.errMsg}")
                        }
                        is LoadingState.Success -> {
                            val response = state.response
                            if (response != null) {
                                onResult(response.id)
                                return@collect
                            }
                        }
                    }
                    ToastUtil.show("[$TAG] 异常")
                    onError()
                }
        }
    }
}