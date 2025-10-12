package com.srap.wash.logic.model

import com.srap.wash.logic.model.base.BaseDataResponse
import com.srap.wash.logic.state.Code

data class OrderListResponse(
    override val code: Code,
    override val data: Data,
    override val msg: String
) : BaseDataResponse<OrderListResponse.Data>(code, data, msg) {
    data class Data(
        val total: Int,
        val items: List<Item>?
    )

    data class Item(
        val id: String,
        val orderNo: String,
        val orderType: Int,
        val orderStatus: Int,
        val createTime: String,
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
        val parentTypeId: String
    )
}