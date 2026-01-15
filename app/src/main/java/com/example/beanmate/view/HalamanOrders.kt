package com.example.beanmate2.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.beanmate2.R
import com.example.beanmate2.data.DataOrder
import com.example.beanmate2.view.viewmodel.OrdersViewModel
import com.example.beanmate2.view.viewmodel.StatusUiOrders
import com.example.beanmate2.view.viewmodel.provider.PenyediaViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanOrders(
    navigateBack: () -> Unit,
    navigateToOrderDetail: (Int) -> Unit,
    refreshTrigger: Boolean,
    onRefreshConsumed: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: OrdersViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    androidx.compose.runtime.LaunchedEffect(refreshTrigger) {
        if (refreshTrigger) {
            viewModel.loadOrders()
            onRefreshConsumed()
        }
    }

    Scaffold(
        topBar = {
            BeanMateTopAppBar(
                title = stringResource(R.string.orders_title),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        }
    ) { innerPadding ->
        OrdersBody(
            statusUiOrders = viewModel.statusUiOrders,
            retryAction = viewModel::loadOrders,
            onOrderClick = navigateToOrderDetail,
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    }
}



@Composable
fun OrdersBody(
    statusUiOrders: StatusUiOrders,
    retryAction: () -> Unit,
    onOrderClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        when (statusUiOrders) {
            is StatusUiOrders.Loading -> {
                CircularProgressIndicator()
            }

            is StatusUiOrders.Error -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = stringResource(R.string.error))
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = retryAction) {
                        Text(text = stringResource(R.string.retry))
                    }
                }
            }

            is StatusUiOrders.Success -> {
                if (statusUiOrders.orders.isEmpty()) {
                    Text(text = "-")
                } else {
                    LazyColumn(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(statusUiOrders.orders, key = { it.id }) { order ->
                            ItemOrder(
                                order = order,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onOrderClick(order.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ItemOrder(
    order: DataOrder,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier, elevation = CardDefaults.cardElevation(2.dp)) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "Order #${order.id}",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Total: Rp ${order.totalPrice}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Status: ${order.status}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Tanggal: ${order.createdAt}",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Tap untuk lihat detail",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
