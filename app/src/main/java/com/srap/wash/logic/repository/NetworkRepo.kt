package com.srap.wash.logic.repository

import android.util.Log
import com.google.gson.Gson
import com.srap.wash.logic.model.base.BaseDataResponse
import com.srap.wash.logic.model.base.BaseListResponse
import com.srap.wash.logic.network.ApiService
import com.srap.wash.logic.network.UserService
import com.srap.wash.logic.state.Code
import com.srap.wash.logic.state.LoadingState
import com.srap.wash.utils.StorageUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@Singleton
class NetworkRepo @Inject constructor(
    @UserService
    private val apiService: ApiService,
) {
    val tag = "NetworkRepo"

    fun getLatestUsed(
        categoryCode: String
    ) = flowList {
        apiService.getLatestUsed(categoryCode, StorageUtil.Token).await()
    }

    fun getGoodsDetails(
        goodsId: Int
    ) = flowData {
        apiService.getGoodsDetails(goodsId, StorageUtil.Token).await()
    }

    fun getGoodsSkus(
        goodsId: Int
    ) = flowList {
        apiService.getGoodsSkus(goodsId, StorageUtil.Token).await()
    }

    fun createTrade(
        promotions: List<Map<String, String>>,
        items: List<Map<String, String>>
    ) = flowData {
        apiService.createTrade(Gson().toJson(promotions), Gson().toJson(items), StorageUtil.Token).await()
    }

    fun prePay(
        orderNo: String,
        payType: Int
    ) = flowData {
        apiService.prePay(orderNo, payType, StorageUtil.Token).await()
    }

    fun getUserInfo() = flowData {
        apiService.getUserInfo(StorageUtil.Token).await()
    }

    suspend fun isLoginStatus() = apiService.getUserInfo(StorageUtil.Token).await()

    fun sendSmsCode(
        phone: String,
    ) = fire {
        Result.success(apiService.sendSmsCode(phone).await())
    }

    fun userReg(
        phone: String,
        verify: String
    ) = flowData {
        apiService.userReg(phone, verify).await()
    }

    fun goodsScan(
        nqt: String
    ) = flowData {
        apiService.goodsScan(nqt, StorageUtil.Token).await()
    }

    fun getOrderList(
        page: Int,
        pageSize: Int = 40,
    ) = flowData {
        apiService.getOrderList(page, pageSize, "", StorageUtil.Token).await()
    }

    fun getOrderDetail(
        orderId: String,
    ) = flowData {
        apiService.getOrderDetail(orderId, StorageUtil.Token).await()
    }

    fun getMessageList(
        subtypeId: Int = 5,
    ) = flowList {
        apiService.getMessageList(subtypeId, StorageUtil.Token).await()
    }

    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) continuation.resume(body)
                    else continuation.resumeWithException(
                        RuntimeException("response body is null")
                    )
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }

    private fun <T> fire(block: suspend () -> Result<T>) = flow {
        val result = try {
            block()
        } catch (e: Exception) {
            Log.e(tag, e.message, e)
            Result.failure(e)
        }
        emit(result)
    }.flowOn(Dispatchers.IO)

    private fun <T> flowData(block: suspend () -> BaseDataResponse<T>) = flow {
        val result = try {
            val response = block()
            if (response.code != Code.SUCCESS) {
                LoadingState.Error(response.msg)
            } else if (response.data != null) {
                LoadingState.Success(response.data)
            } else {
                LoadingState.Error("异常")
            }
        } catch (e: Exception) {
            Log.e(tag, e.message, e)
            LoadingState.Error(e.message ?: "unknown error")
        }
        emit(result)
    }.flowOn(Dispatchers.IO)

    private fun <T> flowList(block: suspend () -> BaseListResponse<T>) = flow {
        val result = try {
            val response = block()
            Log.i(tag, "flowList: $response")
            if (response.code != Code.SUCCESS) {
                LoadingState.Error(response.msg)
            } else if (response.data != null) {
                LoadingState.Success(response.data)
            } else {
                LoadingState.Error("异常")
            }
        } catch (e: Exception) {
            Log.e(tag, e.message, e)
            LoadingState.Error(e.message ?: "unknown error")
        }
        emit(result)
    }.flowOn(Dispatchers.IO)
}