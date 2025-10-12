package com.srap.wash.ui.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.srap.wash.ui.base.BaseRefreshLoadViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> RefreshLoadList(
    viewModel: BaseRefreshLoadViewModel<T>,
    itemWidget: @Composable (item: T) -> Unit,
) {
    // 刷新功能
    val listState = rememberLazyListState()
    val state = rememberPullToRefreshState()

    LaunchedEffect(Unit) {
        if (!viewModel.isLoaded) {
            viewModel.refresh()
        }
    }

    PullToRefreshBox(
        isRefreshing = viewModel.isRefreshing,
        state = state,
        onRefresh = viewModel::refresh
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize().padding(bottom = 5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(viewModel.list) { item ->
                itemWidget(item)
            }

            // 加载更多的指示器
            item {
                if (!viewModel.isRefreshing) {
                    if (viewModel.isEmpty) {
                        Text("已经到底了...")
                    } else {
                        CircularProgressIndicator(modifier = Modifier.padding(6.dp))
                    }
                }
            }
        }
    }

    // 检查是否滚动到底部
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo }
            .collect { layoutInfo ->
                val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
                val totalItemsCount = layoutInfo.totalItemsCount

                // 判断是否滚动到底部并触发加载更多
                if (
                    lastVisibleItemIndex >= totalItemsCount - 1
                    && !viewModel.isLoadMore
                    && !viewModel.isRefreshing
                    && !viewModel.isEmpty
                ) {
                    viewModel.loadMore()
                }
            }
    }
}
