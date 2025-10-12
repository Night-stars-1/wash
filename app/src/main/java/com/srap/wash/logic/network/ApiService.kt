package com.srap.wash.logic.network


import com.srap.wash.logic.model.GoodsDetailsResponse
import com.srap.wash.logic.model.GoodsSkusResponse
import com.srap.wash.logic.model.LatestUsedResponse
import com.srap.wash.logic.model.CreateTradeResponse
import com.srap.wash.logic.model.GoodsScanResponse
import com.srap.wash.logic.model.MessageListResponse
import com.srap.wash.logic.model.OrderDetailResponse
import com.srap.wash.logic.model.OrderListResponse
import com.srap.wash.logic.model.PrePayResponse
import com.srap.wash.logic.model.SendSmsCodeResponse
import com.srap.wash.logic.model.UserInfoResponse
import com.srap.wash.logic.model.UserRegResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("goods/latestUsed")
    fun getLatestUsed(
        @Field("categoryCode") categoryCode: String,
        @Field("token") token: String
    ): Call<LatestUsedResponse>

    @FormUrlEncoded
    @POST("goods/normal/details")
    fun getGoodsDetails(
        @Field("goodsId") goodsId: Int,
        @Field("token") token: String
    ): Call<GoodsDetailsResponse>

    @FormUrlEncoded
    @POST("goods/normal/skus")
    fun getGoodsSkus(
        @Field("goodsId") goodsId: Int,
        @Field("token") token: String
    ): Call<GoodsSkusResponse>

    @FormUrlEncoded
    @POST("trade/create")
    fun createTrade(
        @Field("promotions") promotions: String,
        @Field("items") items: String,
        @Field("token") token: String
    ): Call<CreateTradeResponse>

    @FormUrlEncoded
    @POST("pay/unify/prePay")
    fun prePay(
        @Field("orderNo") orderNo: String,
        @Field("payType") payType: Int,
        @Field("token") token: String
    ): Call<PrePayResponse>

    @FormUrlEncoded
    @POST("user/info")
    fun getUserInfo(
        @Field("token") token: String
    ): Call<UserInfoResponse>

    @FormUrlEncoded
    @POST("common/sms/sendCode")
    fun sendSmsCode(
        @Field("phone") phone: String,
        @Field("template") template: String = "reg"
    ): Call<SendSmsCodeResponse>

    @FormUrlEncoded
    @POST("user/reg")
    fun userReg(
        @Field("phone") phone: String,
        @Field("verify") verify: String,
        @Field("channel") channel: String = "android_app",
    ): Call<UserRegResponse>

    @FormUrlEncoded
    @POST("goods/scan/v2")
    fun goodsScan(
        @Field("NQT") nqt: String,
        @Field("token") token: String,
    ): Call<GoodsScanResponse>

    @FormUrlEncoded
    @POST("order/list")
    fun getOrderList(
        @Field("page") page: Int,
        @Field("pageSize") pageSize: Int,
        @Field("orderStatus") orderStatus: String,
        @Field("token") token: String,
    ): Call<OrderListResponse>

    @FormUrlEncoded
    @POST("order/detail")
    fun getOrderDetail(
        @Field("orderId") orderId: String,
        @Field("token") token: String,
    ): Call<OrderDetailResponse>

    @FormUrlEncoded
    @POST("message/index/list")
    fun getMessageList(
        @Field("subtypeId") subtypeId: Int,
        @Field("token") token: String,
    ): Call<MessageListResponse>
}