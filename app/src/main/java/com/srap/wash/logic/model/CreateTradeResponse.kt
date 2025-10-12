package com.srap.wash.logic.model

import com.srap.wash.logic.model.base.BaseDataResponse
import com.srap.wash.logic.state.Code

data class CreateTradeResponse(
    override val code: Code,
    override val msg: String,
    override val data: Data,
) : BaseDataResponse<CreateTradeResponse.Data>(code, data, msg) {
    data class Data(
        val tradeType: Int,
        val tradeNo: String,
        val orderId: String
    )
}
