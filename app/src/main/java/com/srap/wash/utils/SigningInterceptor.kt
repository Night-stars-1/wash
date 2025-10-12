package com.srap.wash.utils

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class SigningInterceptor : Interceptor {
    private val TAG = "SigningInterceptor"

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        Log.i(TAG, "intercept: $request")
        val path = request.url().encodedPath()
        val channel = "android_app"
        val timestamp = System.currentTimeMillis().toString()
        val token = StorageUtil.Token
        val version = "1.60.3"
        val sign = StringUtil.sha256("appSecret=nFU9pbG8YQoAe1kFh+E7eyrdlSLglwEJeA0wwHB1j5o=&channel=$channel&timestamp=$timestamp&token=$token&version=$version&$path")

        val signedRequest = request.newBuilder()
            .addHeader("Authorization", token)
            .addHeader("Version", version)
            .addHeader("channel", channel)
            .addHeader("timestamp", timestamp)
            .addHeader("sign", sign)
            .build()

        Log.i(TAG,"signedRequest: $signedRequest")

        return chain.proceed(signedRequest)
    }
}
