package com.srap.wash.logic.model

import com.srap.wash.logic.model.base.BaseDataResponse
import com.srap.wash.logic.state.Code

data class OrderDetailResponse(
    override val code: Code,
    override val data: Data,
    override val msg: String
) : BaseDataResponse<OrderDetailResponse.Data>(code, data, msg) {
    data class Data(
        val id: String,
        val orderNo: String,
        val orderType: Int,
        val orderStatus: Int,
        val createTime: String,
        /**
         * 预计完成时间
         */
        val completeTime: String?,
        /**
         * 完成时间
         */
        val finishTime: String?,
        val markPrice: String,
        val payPrice: String,
        val machineType: Int,
        val machineName: String,
        val shopAutoId: Int,
        val shopId: String,
        val shopName: String,
        val isBluetooth: Int,
        val markMinutes: Int,
        val machineFunctionName: String,
        val parentTypeId: String,
        val lineItems: List<LineItem>
    )

    data class LineItem(
        val goodsId: Int,
        val skuId: Int,
        val skuName: String?,
        val originPrice: String?,
        val discountPrice: String?,
        val realPrice: String?
    )
}