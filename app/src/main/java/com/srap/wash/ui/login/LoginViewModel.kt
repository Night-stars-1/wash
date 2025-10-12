package com.srap.wash.ui.login

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import com.srap.wash.logic.repository.NetworkRepo
import com.srap.wash.logic.state.Code
import com.srap.wash.ui.base.BaseViewModel
import com.srap.wash.utils.ToastUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    networkRepo: NetworkRepo,
) : BaseViewModel(networkRepo) {
    private val TAG = javaClass.simpleName

    fun sendSmsCode(phone: String, onResult: () -> Unit) {
        viewModelScope.launch {
            networkRepo.sendSmsCode(phone = phone)
                .collect { state ->
                    val response = state.getOrNull()
                    if (response == null) {
                        ToastUtil.show("[$TAG] 异常")
                    } else if (response.code == Code.SUCCESS) {
                        onResult()
                    } else {
                        ToastUtil.show("[$TAG] ${response.msg}")
                    }
                }
        }
    }
}