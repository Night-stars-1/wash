package com.srap.wash.logic.model

import com.srap.wash.logic.model.base.BaseDataResponse
import com.srap.wash.logic.state.Code

data class GoodsScanResponse(
    override val code: Code,
    override val msg: String,
    override val data: Data?
) : BaseDataResponse<GoodsScanResponse.Data>(code, data, msg) {
    data class Data (
        val id: String,
        val shopId: Int,
        val categoryCode: String,
        val categoryName: String,
        val type: String,
        val shopCanReserve: Boolean,
        val shopWhitelist: String?,
        val mpPositionType: Int,
        val shopType: Int,
        val jumpToAlipayFlag: Boolean,
        val jumpToAlipayFlag2: Boolean
    )
}
