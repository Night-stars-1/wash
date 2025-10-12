package com.srap.wash.ui.goodsdetail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.srap.wash.logic.model.GoodsDetailsResponse
import com.srap.wash.logic.repository.NetworkRepo
import com.srap.wash.logic.state.LoadingState
import com.srap.wash.ui.base.BaseViewModel
import com.srap.wash.utils.ToastUtil
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = GoodsDetailViewModel.ViewModelFactory::class)
class GoodsDetailViewModel @AssistedInject constructor(
    @Assisted val goodsId: Int,
    networkRepo: NetworkRepo,
) : BaseViewModel(networkRepo) {
    private val TAG = javaClass.simpleName

    var data by mutableStateOf<GoodsDetailsResponse.Data?>(null)
        private set

    init {
        fetchGoodsDetail()
    }

    @AssistedFactory
    interface ViewModelFactory {
        fun create(goodsId: Int): GoodsDetailViewModel
    }


    private fun fetchGoodsDetail() {
        viewModelScope.launch {
            networkRepo.getGoodsDetails(goodsId)
                .collect { state ->
                    when (state) {
                        is LoadingState.Error -> {
                            ToastUtil.show("[$TAG] ${state.errMsg}")
                        }
                        is LoadingState.Success -> {
                            data = state.response
                        }
                    }
                }
        }
    }
}