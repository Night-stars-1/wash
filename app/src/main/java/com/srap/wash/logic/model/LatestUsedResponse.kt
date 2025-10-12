package com.srap.wash.logic.model

import com.srap.wash.logic.model.base.BaseListResponse
import com.srap.wash.logic.state.Code

data class LatestUsedResponse(
    override val code: Code,
    override val data: List<Data>,
    override val msg: String
) : BaseListResponse<LatestUsedResponse.Data>(code, data, msg) {
    data class Data(
        val goodsId: Int,
        val goodsName: String,
        val orgId: Int,
        val orgName: String,
        val workStatus: Int
    )
}
