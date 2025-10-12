package com.srap.wash.logic.model.base

import com.srap.wash.logic.state.Code

abstract class BaseDataResponse<T>(code: Code, data: T?, msg: String) {
    // 非公开字段，防止被序列化
    private val _code: Code = code
    private val _data: T? = data
    private val _msg: String = msg

    // 提供公共的 getter 方法
    open val code: Code get() = _code
    open val data: T? get() = _data
    open val msg: String get() = _msg
}