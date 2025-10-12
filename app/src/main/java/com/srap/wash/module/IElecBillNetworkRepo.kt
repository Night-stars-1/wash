package com.srap.wash.module

import com.srap.wash.logic.model.ReserveResponse
import kotlinx.coroutines.flow.Flow

interface IElecBillNetworkRepo {
    fun getReserve(): Flow<Result<ReserveResponse>>

    interface Provider {
        fun get(): IElecBillNetworkRepo
    }
}