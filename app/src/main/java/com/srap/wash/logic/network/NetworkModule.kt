package com.srap.wash.logic.network

import com.google.gson.GsonBuilder
import com.srap.wash.logic.state.Code
import com.srap.wash.logic.state.CodeAdapter
import com.srap.wash.utils.SigningInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UserService

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val USER_BASE_URL = "https://userapi.qiekj.com"

    @UserService
    @Singleton
    @Provides
    fun provideUserService(@UserService retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @UserService
    @Singleton
    @Provides
    fun provideUserServiceRetrofit(@UserService okHttpClient: OkHttpClient): Retrofit {
        val gson = GsonBuilder()
            .registerTypeAdapter(Code::class.java, CodeAdapter()) // 注册自定义 TypeAdapter
            .create()

        return Retrofit.Builder()
            .baseUrl(USER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
    }

    @UserService
    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(SigningInterceptor()) // 添加请求签名拦截器
            .build()
    }
}