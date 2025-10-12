package com.srap.wash.ui.base

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.srap.wash.logic.repository.NetworkRepo

abstract class BaseRefreshLoadViewModel<T>(
    override val networkRepo: NetworkRepo,
) : BaseViewModel(networkRepo) {

    var list by mutableStateOf(listOf<T>())

    var isEmpty by mutableStateOf(false)
    var isRefreshing by mutableStateOf(false)
    var isLoadMore by mutableStateOf(false)

    /**
     * 当前页数，不会自动增加
     */
    var page = 1

    open fun fetchData() {
        isLoaded = true
        isLoadMore = false
        isRefreshing = false
    }

    open fun refresh() {
        if (!isRefreshing && !isLoadMore) {
            list = emptyList()
            page = 1
            isLoadMore = false
            isRefreshing = true
            fetchData()
        }
    }

    open fun loadMore() {
        if (!isRefreshing && !isLoadMore) {
            isLoadMore = true
            isRefreshing = false
            fetchData()
        }
    }
}