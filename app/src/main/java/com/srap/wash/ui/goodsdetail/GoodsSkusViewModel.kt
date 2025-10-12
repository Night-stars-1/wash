package com.srap.wash.ui.goodsdetail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.srap.wash.logic.model.GoodsSkusResponse
import com.srap.wash.logic.repository.NetworkRepo
import com.srap.wash.logic.state.LoadingState
import com.srap.wash.ui.base.BaseViewModel
import com.srap.wash.utils.ToastUtil
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = GoodsSkusViewModel.ViewModelFactory::class)
class GoodsSkusViewModel @AssistedInject constructor(
    @Assisted val goodsId: Int,
    networkRepo: NetworkRepo,
) : BaseViewModel(networkRepo) {
    private val TAG = javaClass.simpleName

    var data by mutableStateOf<List<GoodsSkusResponse.Data>>(emptyList())
        private set

    init {
        fetchGoodsSkus()
    }

    @AssistedFactory
    interface ViewModelFactory {
        fun create(goodsId: Int): GoodsSkusViewModel
    }


    private fun fetchGoodsSkus() {
        viewModelScope.launch {
            networkRepo.getGoodsSkus(goodsId)
                .collect { state ->
                    when (state) {
                        is LoadingState.Error -> {
                            ToastUtil.show("[$TAG] ${state.errMsg}")
                        }
                        is LoadingState.Success -> {
                            val goodsList = state.response
                            if (!goodsList.isNullOrEmpty()) {
                                data = goodsList
                                return@collect
                            }
                            ToastUtil.show("[$TAG] 数据异常")
                        }
                    }
                }
        }
    }
}