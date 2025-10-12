/**
 * 这里是用于加载动态模块的方法
 * 因为这个是作者宿舍的电费查询模块，所以这部分不开源
 * 采用动态模块的形式加载
 */
package com.srap.wash.module

import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

const val PROVIDER_CLASS = "com.srap.elecbill.module.ElecBillModule"

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private var networkRepo: IElecBillNetworkRepo? = null

    @Provides
    @Singleton
    fun provideNetworkRepo(): IElecBillNetworkRepo? {
        if (networkRepo != null){
            return networkRepo as IElecBillNetworkRepo
        }
        try {
            val provider = Class.forName(PROVIDER_CLASS).kotlin.objectInstance as IElecBillNetworkRepo.Provider
            return provider.get()
                .also { networkRepo = it }
        } catch (e: ClassNotFoundException){
            Log.e("TAG", "Provider class not found", e)
            return null
        }
    }
}