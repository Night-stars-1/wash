package com.srap.wash.logic.model

import com.srap.wash.logic.model.base.BaseDataResponse
import com.srap.wash.logic.state.Code

data class UserInfoResponse(
    override val code: Code,
    override val msg: String,
    override val data: Data?,
) : BaseDataResponse<UserInfoResponse.Data>(code, data, msg) {
    data class Data(
        val autoId: Long,
        val id: String,
        val userName: Any?,
        val sex: Int,
        val birthday: Any?,
        val phone: String,
        val headImageId: Any?,
        val balance: String,
        val unCompleteOrders: Any?,
        val hasVip: Boolean,
        val registerTime: String,
        val newBirthday: Any?,
        val gender: Int,
        val vocation: Any?,
        val vocationId: Any?,
        val hasCertificate: Boolean
    )
}