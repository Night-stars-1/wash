package com.srap.wash.logic.model

import com.srap.wash.logic.model.base.BaseListResponse
import com.srap.wash.logic.state.Code

data class GoodsSkusResponse(
    override val code: Code,
    override val data: List<Data>,
    override val msg: String
) : BaseListResponse<GoodsSkusResponse.Data>(code, data, msg) {
    data class Data(
        val skuId: Int,
        val name: String,
        val feature: String,
        val price: String,
        val vipDiscountPrice: String,
        val discountPrice: String,
        val vipDiscount: String,
        val discount: String,
        val unit: String,
        val stockState: Int
    )
}
