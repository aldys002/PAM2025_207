package com.example.beanmate2.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.beanmate2.R
import com.example.beanmate2.data.DataOrderItem
import com.example.beanmate2.view.viewmodel.OrderDetailViewModel
import com.example.beanmate2.view.viewmodel.StatusUiOrderDetail
import com.example.beanmate2.view.viewmodel.provider.PenyediaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanOrderDetail(
    orderId: Int,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: OrderDetailViewModel = viewModel(
        factory = PenyediaViewModel.Factory,
        key = "OrderDetailViewModel_$orderId"
    )
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.loadDetail()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    Scaffold(
        topBar = {
            BeanMateTopAppBar(
                title = stringResource(R.string.order_detail_title),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (val state = viewModel.statusUiOrderDetail) {
                is StatusUiOrderDetail.Loading -> CircularProgressIndicator()
                is StatusUiOrderDetail.Error -> Text(text = stringResource(R.string.error))
                is StatusUiOrderDetail.Success -> {
                    val data = state.detail.data ?: run {
                        Text(text = stringResource(R.string.error))
                        return@Box
                    }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        // HEADER
                        item {
                            Text(
                                text = "Order #${data.order.id}",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                text = "Tanggal: ${data.order.createdAt}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        item {
                            Card {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = "Data Pemesan",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text("Nama: ${data.order.nama}")
                                    Text("Alamat: ${data.order.alamat}")
                                    Text("Telepon: ${data.order.telepon}")
                                }
                            }
                        }
                        item {
                            Card {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = "Ringkasan Pesanan",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text("Status: ${data.order.status}")
                                    Text(
                                        text = "Total: Rp ${data.order.totalPrice}",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                        item {
                            Text(
                                text = "Daftar Item",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        items(
                            items = data.items,
                            key = { it.productId }
                        ) { item -> ItemOrderDetail(item = item) }
                    }
                }
            }
        }
    }
}

@Composable
fun ItemOrderDetail(
    item: DataOrderItem,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = item.name, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Qty: ${item.qty}")
            Text(text = "Harga: Rp ${item.priceEach}")
            Text(text = "Subtotal: Rp ${item.subtotal}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
