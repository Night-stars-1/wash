package com.srap.wash.logic.model

import com.srap.wash.logic.model.base.BaseDataResponse
import com.srap.wash.logic.state.Code

data class SendSmsCodeResponse(
    override val code: Code,
    override val msg: String,
    override val data: Nothing?,
) : BaseDataResponse<PrePayResponse.Data>(code, data, msg)
