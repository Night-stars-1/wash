package com.srap.wash.logic.model

import com.srap.wash.logic.model.base.BaseDataResponse
import com.srap.wash.logic.state.Code

data class UserRegResponse(
    override val code: Code,
    override val msg: String,
    override val data: Data?,
) : BaseDataResponse<UserRegResponse.Data>(code, data, msg) {
    data class Data(
        val token: String
    )
}
