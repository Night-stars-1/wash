package com.srap.wash.logic.model

import com.srap.wash.logic.model.base.BaseDataResponse
import com.srap.wash.logic.state.Code

data class GoodsDetailsResponse(
    override val code: Code,
    override val data: Data,
    override val msg: String
) : BaseDataResponse<GoodsDetailsResponse.Data>(code, data, msg) {
    data class Data(
        val goodsId: Int,
        val name: String,
        val categoryCode: String,
        val subCategoryId: Int,
        val orgId: Int,
        val orgName: String,
        val orgAttribute: Int,
        val positionId: Int,
        val soldState: Int,
        val stockType: Int,
        val payTypes: List<Int>,
        val createTime: String,
        val deviceId: Int,
        val communication: String,
        val brandName: String,
        val brandLogo: String,
        val brandExposure: String,
        val serviceTelephone: String,
    )
}
