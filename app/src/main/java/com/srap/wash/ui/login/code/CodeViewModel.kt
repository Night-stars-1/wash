package com.srap.wash.ui.login.code

import androidx.lifecycle.viewModelScope
import com.srap.wash.logic.model.UserRegResponse
import com.srap.wash.logic.repository.NetworkRepo
import com.srap.wash.logic.state.LoadingState
import com.srap.wash.ui.base.BaseViewModel
import com.srap.wash.utils.ToastUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CodeViewModel @Inject constructor(
    networkRepo: NetworkRepo,
) : BaseViewModel(networkRepo) {

    fun userReg(phone: String, code: String, onResult: (token: String) -> Unit) {
        viewModelScope.launch {
            networkRepo.userReg(phone = phone, verify = code)
                .collect { state ->
                    when (state) {
                        is LoadingState.Error -> {
                            ToastUtil.show(state.errMsg)
                        }
                        is LoadingState.Success -> {
                            if (state.response != null) {
                                onResult(state.response.token)
                            } else {
                                ToastUtil.show("数据异常")
                            }
                        }
                    }
                }
        }
    }
}