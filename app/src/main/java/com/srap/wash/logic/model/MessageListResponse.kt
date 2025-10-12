package com.srap.wash.logic.model

import com.srap.wash.logic.model.base.BaseListResponse
import com.srap.wash.logic.state.Code

data class MessageListResponse(
    override val code: Code,
    override val data: List<Data>,
    override val msg: String,
) : BaseListResponse<MessageListResponse.Data>(code, data, msg) {
    data class Data(
        val message: Message?,
        val order: Order?
    )

    data class Order(
        /**
         * 订单号
         */
        val orderNo: String,
        val goodsCategory: String,
        val shopName: String,
        val skuName: String,
        val goodsName: String,
        /**
         * 完成时间
         */
        val finishTime: String
    )

    data class Message(
        val id: Int,
        val userId: Long,
        val content: String,
        val subtypeId: Int,
        val msgKey: String,
        val read: Int,
        val createTime: String,
        val readTime: String
    )
}
