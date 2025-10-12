package com.srap.wash.logic.model

import com.srap.wash.logic.model.base.BaseDataResponse
import com.srap.wash.logic.state.Code

data class PrePayResponse(
    override val code: Code,
    override val msg: String,
    override val data: Data
) : BaseDataResponse<PrePayResponse.Data>(code, data, msg) {
    data class Data(
        val exist: Boolean,
        val messageId: String,
        val gateway: String,
        val prepayParam: String,
    )
}
